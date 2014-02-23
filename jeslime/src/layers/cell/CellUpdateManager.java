package layers.cell;

import java.util.HashSet;

import structural.Flags;
import structural.identifiers.Coordinate;
import cells.Cell;

/**
 * 
 * @test CellUpdaterManagerTest
 * @author David Bruce Borenstein
 *
 */
public class CellUpdateManager {
	private CellLayerContent content;
	private CellLayerIndices indices;
	
	public CellUpdateManager(CellLayerContent content, CellLayerIndices indices) {
		this.content = content;
		this.indices = indices;
		
	}
	
	/**
	 * Instructs the specified cell to calculate its next state. Returns number
	 * of calls to consider since last call to apply (including this one). This
	 * should not change the apparent state of the cell. Therefore no indices
	 * are updated.
	 * 
	 * @param coord
	 * @return
	 */
	public int consider(Coordinate coord) {
	    Cell cell = content.get(coord);
		int res = cell.consider();
	    return res;
	}
	
	/**
	 * Instructs the specified cell to update its state. Should blow up if
	 * the cell has not called consider since last apply call. You should
	 * USE THIS METHOD for updating cells, rather than doing so directly.
	 * 
	 * @param coord
	 */
	public void apply(Coordinate coord) {
	    Cell cell = content.get(coord);
	    
	    // Decrement CURRENT cell state count
		indices.decrStateCount(cell);
		
	    cell.apply();

	    // Increment NEW cell state count, which may be the same as the old one
	    indices.incrStateCount(cell);
	    
	    refreshDivisibility(coord);
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
		content.checkExists(cCoord);

		// Note: divide(...) updates state index for parent
		Cell child = divide(pCoord);
		
		// Attempt to place child
		// Note: place(...) updates state index for child
		place(child, cCoord);

		// Update divisibility index
		refreshDivisibility(pCoord);
		refreshDivisibility(cCoord);
	}
	
	// TODO: The exposure of this method is a bit of cloodge for the shoving
	// method. There's no obvious way around it as things stand, but it does
	// suggest that a refactor may soon be necessary.
	public Cell divide(Coordinate pCoord) {

		content.checkExists(pCoord);

		// Make sure cell is divisible
		if (!indices.isDivisible(pCoord)) {
			throw new IllegalStateException("Attempting to divide non-divisible cell.");
		}

		// Divide parent
		Cell parent = content.get(pCoord);
		indices.decrStateCount(parent);
		Cell child = parent.divide();
		indices.incrStateCount(parent);

		refreshDivisibility(pCoord);
		
		return child;
	}
	
	/**
	 * Place the specified cell at the specified coordinate. 
	 * @param cell
	 * @param coord
	 */
	public void place(Cell cell, Coordinate coord) {

		content.checkExists(coord);

		if (indices.isOccupied(coord)) {
			throw new IllegalStateException("Illegal state: Attempting to place a cell into an occupied site at " + coord.toString() + ".");
		}

		// Place cell in cell lattice
		content.put(coord, cell);
		indices.incrStateCount(cell);
		
		// Update occupancy indices
		indices.getOccupiedSites().add(coord);
        indices.getCellLocationIndex().place(cell, coord);

        // If cell is divisible, add to divisibility index
		refreshDivisibility(coord);
	}
	
	/**
	 * Remove the specified cell from the lattice. 
	 * @param coord
	 */
	public void banish(Coordinate coord) {
		content.checkExists(coord);
        Cell toRemove = content.get(coord);

		if ((!coord.hasFlag(Flags.END_OF_WORLD)) && (!indices.isOccupied(coord))) {
			throw new IllegalStateException("Attempting to banish cell at empty site");
		}

		indices.decrStateCount(content.get(coord));

		indices.getOccupiedSites().remove(coord);

		content.put(coord, null);
        indices.getCellLocationIndex().remove(toRemove);
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
		System.out.println("       Swapping " + pCoord + " and " + qCoord +".");
		
		pCoord = pCoord.canonicalize();
		qCoord = qCoord.canonicalize();
		
		// When swapping a cell with itself, just return
		if (pCoord.equals(qCoord)) {
			return;
		}

		// If both sites are vacant, return

		else if (!indices.isOccupied(pCoord) && !indices.isOccupied(qCoord)) {

			return;
		}

		// If p is vacant and q is not, move q to p.
		else if (!indices.isOccupied(pCoord) && indices.isOccupied(qCoord)) {

			move(qCoord, pCoord);

			return;
		}

		// If q is vacant and p is not, move p to q.
		else if (indices.isOccupied(pCoord) && !indices.isOccupied(qCoord)) {

			move(pCoord, qCoord);

			return;
		}

		// If they are both occupied, swap.
		else if (indices.isOccupied(pCoord) && indices.isOccupied(qCoord)) {

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
		content.checkExists(pCoord);
		content.checkExists(qCoord);

		// Validate
		if (!indices.isOccupied(pCoord)) {
			throw  new IllegalStateException("Attempting to move a vacant cell.");
		}
		if (indices.isOccupied(qCoord)) {
			throw new IllegalStateException("Attempting to move cell to an occupied site.");
		}

		// Get cell
		Cell cell = content.get(pCoord);
		
		// Remove cell from old location
		content.put(pCoord, null);
		indices.getOccupiedSites().remove(pCoord);
		
		// Add it to its new location
		content.put(qCoord, cell);
		indices.getOccupiedSites().add(qCoord);

        indices.getCellLocationIndex().move(cell, qCoord);

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

		content.checkExists(pCoord);
		content.checkExists(qCoord);

	    // Validate
		if (!indices.isOccupied(pCoord)) {
	        throw new IllegalStateException("Called swap on a vacant site. Use 'move' for this purpose.");
	    }
		if (!indices.isOccupied(qCoord)) {
	        throw new IllegalStateException("Called swap on a vacant site. Use 'move' for this purpose.");
	    }

        // Identify cells
        Cell p = content.get(pCoord);
        Cell q = content.get(qCoord);

	    // Swap
		content.put(pCoord, q);
		content.put(qCoord, p);

        // Update location index
        indices.getCellLocationIndex().move(p, qCoord);
        indices.getCellLocationIndex().move(q, pCoord);

	    // Update divisibility index
		refreshDivisibility(pCoord);
		refreshDivisibility(qCoord);
	}

	
	public void refreshDivisibility(Coordinate coord) {
		content.checkExists(coord);
		
		boolean divisible = indices.isOccupied(coord) && content.get(coord).isDivisible();
		CellIndex div = indices.getDivisibleSites();

		if (div.contains(coord) && !divisible) {
			div.remove(coord);
		}
		
		if (!div.contains(coord) && divisible) {
			div.add(coord);
		}
	}
}
