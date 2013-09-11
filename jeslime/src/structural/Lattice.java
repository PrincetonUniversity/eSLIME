package structural;

import geometries.Geometry;
import cells.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import structural.identifiers.Coordinate;

public class Lattice {

	// Geometry (passed to Lattice at construction time)
    private Geometry geom;

	// All sites (basically the keyset for the lattice map). 
	private Coordinate[] canonicalSites;

	// Mapping of sites to cell pointers -- the lattice itself.		
	private HashMap<Coordinate, Cell> map;

	// Coordinate sets. We often need to know all the sites that
	// have a particular property so we can iterate over them, 
	// so we track them as sets.

	// Occupied sites (non-vacant sites)
	private HashSet<Coordinate> occupiedSites;

	// Divisible sites
	private HashSet<Coordinate> divisibleSites;
	
	public Lattice(Geometry geom) {
		// Assign geometry
	    this.geom = geom;

		// Get canonical site list
	    canonicalSites = geom.getCanonicalSites();

		// Initialize data structures
		map = new HashMap<Coordinate, Cell>();
		occupiedSites = new HashSet<Coordinate>();
		divisibleSites = new HashSet<Coordinate>();
				
		// Initialize conditional indices
	    for (int i = 0; i < canonicalSites.length; i++) {
			Coordinate coord = canonicalSites[i];

			// Initialize each site to null (empty)
			map.put(coord, null);	
		}
	}

	/**
	 * Returns the fitness value of the cell at the specified coordinate.
	 * @param coord
	 * @return
	 */
	public double getFitness(Coordinate coord) {
	    Cell cell = getCellAt(coord);

		// Return fitness of cell
	    return cell.getFitness();
	}

	/**
	 * Returns the state of the cell at the specified coordinate.
	 * @param coord
	 * @return
	 */
	public int getState(Coordinate coord) {
	    Cell cell = getCellAt(coord);

	    return cell.getState();
	}

	/**
	 * Get the state of neighboring cells. Vacant cells are ignored.
	 * @param coord
	 * @return
	 */
	public int[] getNeighborStates(Coordinate coord) {
		checkExists(coord);

	    // Get set of neighbors
		Coordinate[] neighbors = geom.getCellNeighbors(coord);

		// Allocate return vector
		int[] states = new int[neighbors.length];

		// Check state of each neighbor
	    for (int i = 0; i < neighbors.length; i++) {
			Coordinate query = neighbors[i];
			states[i] = getState(query);
		}

		// Return
		return states;
	}

	/**
	 * Get the site or sites with the minimum L1 (Manhattan) distance,
	 * up to the specified maximum distance. If maxDistance is -1, the
	 * search is unbounded.
	 * @param coord
	 * @param maxDistance
	 * @return
	 */
	// TODO: Create a second function that doesn't have the second
	// argument for unbounded searching?
	public Coordinate[] getNearestVacancies(Coordinate coord, int maxDistance) {

		checkExists(coord);


		// If there are no vacancies, just return now. This should prevent infinite
		// loop even when searching without bound.
		if (occupiedSites.size() > canonicalSites.length) {
			throw new IllegalStateException("Consistency failure.");
		} else if (occupiedSites.size() == canonicalSites.length) {
	    	return new Coordinate[0];
		}

		// Initialize return object
		ArrayList<Coordinate> res = new ArrayList<Coordinate>();
		
		// Loop through looking for vacancies (starting with target site)
		int r = 0;

		// I included this extra map so I could check for duplicates in best 
		// case O(1) time, but if I have to do that, doesn't it seem like I should
		// be returning a set instead of building two data structures?
		HashSet<Coordinate> incl = new HashSet<Coordinate>();

		while ((maxDistance == -1) || (r <= maxDistance)) {

			// We want to check every site, so don't use circumnavigation restriction.
			Coordinate[] annulus = geom.getAnnulus(coord, r, false);


	        for (int i = 0; i < annulus.length; i++) {

				Coordinate query = annulus[i];

				// Sanity check
				checkExists(query);

				if (!occupiedSites.contains(query) && !incl.contains(query)) {
			
					incl.add(query);
					res.add(query);
				}

			}

			// If we've managed to populate res, it means that we founds targets
			// in the current annulus, so return.
			if (res.size() > 0) {
				return(res.toArray(new Coordinate[0]));
			}

			r++;
		} 

		return(res.toArray(new Coordinate[0]));
	}

	/**
	 * Returns a vector containing the canonical coordinate of each
	 * site on the lattice.
	 * @return
	 */
	public Coordinate[] getCanonicalSites() {
		// Construct a copy of internal state
		Coordinate[] res = new Coordinate[canonicalSites.length];
		for (int i = 0; i < canonicalSites.length; i++)
			res[i] = canonicalSites[i];
		
		// Return it
		return res;
	}

	/**
	 * Returns a vector containing the canonical coordinate of each
	 * active site on the lattice.
	 * @return
	 */
	public HashSet<Coordinate> getOccupiedSites() {
		// Construct a copy of internal state
		HashSet<Coordinate> res = new HashSet<Coordinate>(occupiedSites);

		// Return it
		return res;
	}

	/**
	 * Returns a list of divisible sites on the lattice.
	 * 
	 * @return
	 */
	public HashSet<Coordinate> getDivisibleSites() {
		// Construct a copy of internal state
		HashSet<Coordinate> res = new HashSet<Coordinate>(divisibleSites);

		// Return it
		return res;
	}

	/**
	 * Instructs the specified cell to calculate its next state. Returns number
	 * of calls to consider since last call to apply (including this one).
	 * @param coord
	 * @return
	 */
	public int consider(Coordinate coord) {
		// TODO Verify that there is no outstanding lock.
	    Cell cell = getCellAt(coord);
		int res = cell.consider();
	    return res;
	}

	/**
	 * Instructs the specified cell to update its state. Should blow up if
	 * the cell has not called consider since last apply call.
	 * @param coord
	 */
	public void apply(Coordinate coord) {
		// TODO Verify that there is no outstanding lock.
	    Cell cell = getCellAt(coord);
	    cell.apply();

	    refreshDivisibility(coord);
	}

	private void refreshDivisibility(Coordinate coord) {

		checkExists(coord);

		HashSet<Coordinate> div = divisibleSites;

		// If cell was divisible and is now dead, remove it from list
		if (div.contains(coord) && !occupiedSites.contains(coord)) {
			div.remove(coord);
		}

		if (occupiedSites.contains(coord)) {
			Cell cell = map.get(coord);

	        // If cell was divisible and no longer is, remove it from list
			if (div.contains(coord) && !(cell.isDivisible())) {
				div.remove(coord);

	        // If cell was not divisible but now is, add it to the list
			} else if (!div.contains(coord) && cell.isDivisible()) {
				div.add(coord);
	        }
	    }
	}

	/**
 	 * Request a daughter cell from the specified cell, and place it at the
 	 * specified location. divideTo(...) may trigger physical state changes
 	 * on the part of the parent cell.
 	 * 
	 * Note: the following methods return false if unsuccessful for expected
 	 * reasons (e.g., lattice full). The definiton of an "expected reason"
 	 * depends on the cell type. Such events typically are used to signal
 	 * that the simulation is over. However, they are designed to throw
 	 * exceptions if something erroneous occurs, such as a call to a null
 	 * cell. Note however that "position occupied" is a handled condition.
 	 * If this should be considered erroneous, an exception should be thrown
 	 * by the calling function.
 	 * 
	 * @param pCoord
	 * @param cCoord
	 */
	public void divideTo(Coordinate pCoord, Coordinate cCoord) {
		checkExists(cCoord);

		Cell child = divide(pCoord);

		// Attempt to place child
		place(child, cCoord);

		// Update divisibility index
		refreshDivisibility(pCoord);
		refreshDivisibility(cCoord);
	}

	// TODO: The exposure of this method is a bit of cloodge for the shoving
	// method. There's no obvious way around it as things stand, but it does
	// suggest that a refactor may soon be necessary.
	public Cell divide(Coordinate pCoord) {

		checkExists(pCoord);

		// Make sure cell is divisible
		if (!divisibleSites.contains(pCoord)) {
			throw new IllegalStateException("Attempting to divide non-divisible cell.");
		}

		// Divide parent
		Cell parent = getCellAt(pCoord);
		Cell child = parent.divide();

		return child;
	}

	// The following update methods do not notify the affected cell or its
	// neighbors that the environment has changed!
	
	/**
	 * Place the specified cell at the specified coordinate. 
	 * @param cell
	 * @param coord
	 */
	public void place(Cell cell, Coordinate coord) {

		// TODO Verify that there is no outstanding lock.
		checkExists(coord);

		if (occupiedSites.contains(coord)) {
			throw new IllegalStateException("Illegal state: Attempting to place a cell into an occupied site at " + coord.toString() + ".");
		}

		// Place cell in cell lattice
		map.put(coord, cell);
		
		// Update occupancy indices
		(occupiedSites).add(coord);

		// If cell is divisible, add to divisibility index
		refreshDivisibility(coord);
	}

	/**
	 * Remove the specified cell from the lattice. 
	 * @param coord
	 */
	public void banish(Coordinate coord) {
		// TODO Verify that there is no outstanding lock.
		checkExists(coord);

		if ((!coord.hasFlag(Flags.END_OF_WORLD)) && (!occupiedSites.contains(coord))) {
			throw new IllegalStateException("Attempting to banish cell at empty site");
		}

		(occupiedSites).remove(coord);
		map.put(coord, null);

		refreshDivisibility(coord);
	}

	/**
	 * Swaps the contents of the two specified coordinates using either move
	 * or swap, as appropriate. If the two are both empty, nothing happens.
	 * This operation should always be successful, but has fewer checks.
	 * 
	 * @param pCoord
	 * @param qCoord
	 */

	public void f_swap(Coordinate pCoord, Coordinate qCoord) {
		// When swapping a cell with itself, just return
		if (pCoord.equals(qCoord)) {
			return;
		}

		// If both sites are vacant, return

		else if (!occupiedSites.contains(pCoord) && !occupiedSites.contains(qCoord)) {

			return;
		}

		// If p is vacant and q is not, move q to p.
		else if (!occupiedSites.contains(pCoord) && occupiedSites.contains(qCoord)) {

			move(qCoord, pCoord);

			return;
		}

		// If q is vacant and p is not, move p to q.
		else if (occupiedSites.contains(pCoord) && !occupiedSites.contains(qCoord)) {

			move(pCoord, qCoord);

			return;
		}

		// If they are both occupied, swap.
		else if (occupiedSites.contains(pCoord) && occupiedSites.contains(qCoord)) {

			swap(pCoord, qCoord);

			return;
		}

		// Should never get here.
		throw new IllegalStateException("If you see this, you forgot a case in f_swap.");
	}


	/**
	 * Relocate the cell at the first coordinate to the second coordinate.
	 * This method blows up if the target site is occupied.
	 *  
	 * @param pCoord
	 * @param qCoord
	 */
	public void move(Coordinate pCoord, Coordinate qCoord) {

		// TODO Verify that there is no outstanding lock.
		checkExists(pCoord);
		checkExists(qCoord);

		// Validate
		if (!occupiedSites.contains(pCoord)) {
			throw  new IllegalStateException("Attempting to move a vacant cell.");
		}
		if (occupiedSites.contains(qCoord)) {
			throw new IllegalStateException("Attempting to move cell to an occupied site.");
		}

		HashMap<Coordinate, Cell> m = map;

		// Get cell
		Cell cell = m.get(pCoord);
		
		// Remove cell from old location
		m.put(pCoord, null);
		(occupiedSites).remove(pCoord);
		
		// Add it to its new location
		m.put(qCoord, cell);
		(occupiedSites).add(qCoord);

		// Update divisibility indices
		refreshDivisibility(pCoord);
		refreshDivisibility(qCoord);
	}

	/**
	 * Swap the cells at the specified locations.
	 * @param pCoord
	 * @param qCoord
	 */
	public void swap(Coordinate pCoord, Coordinate qCoord) {

		checkExists(pCoord);
		checkExists(qCoord);

	    // Validate
		if (!occupiedSites.contains(pCoord)) {
	        throw new IllegalStateException("Called swap on a vacant site. Use 'move' for this purpose.");
	    }
		if (!occupiedSites.contains(qCoord)) {
	        throw new IllegalStateException("Called swap on a vacant site. Use 'move' for this purpose.");
	    }

		HashMap<Coordinate, Cell> m = map;

	    // Swap
		Cell swap = m.get(pCoord);
		m.put(pCoord, m.get(qCoord));
		m.put(qCoord, swap);

	    // Both sites are occupied, so the only index to update is divisibility.
		refreshDivisibility(pCoord);
		refreshDivisibility(qCoord);
	}

	private Coordinate checkExists(Coordinate coord) {
		
		// First check to see if this cell is supposed to be retained even though
		// it is not a "real" coordinate.
		if (coord.hasFlag(Flags.END_OF_WORLD)) {
			return coord;
		}

		// Otherwise, it had better be in the coordinate system.
		if (!map.containsKey(coord)) {
			StringBuilder ss = new StringBuilder();
			ss.append("Consistency failure: coordinate ");
			ss.append(coord.stringForm());
			ss.append(" is expected to be well-defined in this geometry, but is not.\n");
			String str = ss.toString();
			throw new IllegalStateException(str);
		}
		return coord;
	}

	private Cell getCellAt(Coordinate coord) {
		checkExists(coord);

		// Validate input
		if (!occupiedSites.contains(coord))
			throw new IllegalStateException("Attempted to access an empty cell.");

	    // Get pointer to cell and return it
		Cell res = map.get(coord);

	    return res;
	}

	public int[] getStateVector() {
		Coordinate[] cArr = getCanonicalSites();
		
		int[] sArr = new int[cArr.length];
		
		for (int i = 0; i < cArr.length; i++) {
			Cell c = map.get(cArr[i]);
			if (c == null) { 
				sArr[i] = 0;
			} else {
				sArr[i] = map.get(cArr[i]).getState();
			}
		}
		
		return sArr;
	}
	
	public double[] getFitnessVector() {
		Coordinate[] cArr = getCanonicalSites();
		
		double[] fArr = new double[cArr.length];
		
		for (int i = 0; i < cArr.length; i++) {
			Cell c = map.get(cArr[i]);
			if (c == null) { 
				fArr[i] = 0D;
			} else {
				fArr[i] = map.get(cArr[i]).getFitness();
			}
		}
		
		return fArr;
	}
	
}
