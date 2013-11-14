package layers.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cells.Cell;
import structural.identifiers.Coordinate;

/**
 * Direct representation of cell layer indices. Only the cell layer and
 * its content object should have direct access to this object. Processes
 * should use a CellLayerViewer.
 * 
 * @test CellLayerIndicesTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayerIndices {

	// Coordinate sets. We often need to know all the sites that
	// have a particular property so we can iterate over them, 
	// so we track them as sets.

	// Occupied sites (non-vacant sites)
	protected CellIndex occupiedSites;

	// Divisible sites
	protected CellIndex divisibleSites;
	
	// Map that tracks count of cells with each state
	protected HashMap<Integer, Integer> stateMap;
		
	public CellLayerIndices() {
		occupiedSites = new CellIndex();
		divisibleSites = new CellIndex();
		stateMap = new HashMap<Integer, Integer>();
	}
	
	public boolean isOccupied(Coordinate cell) {
		Coordinate c = cell.canonicalize();
		if (occupiedSites.contains(c)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDivisible(Coordinate cell) {
		Coordinate c = cell.canonicalize();
		if (divisibleSites.contains(c)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a vector containing the canonical coordinate of each
	 * active site on the lattice.
	 * @return
	 */
	public CellIndex getOccupiedSites() {
		return occupiedSites;
	}
	
	/**
	 * Returns a list of divisible sites on the lattice.
	 * 
	 * @return
	 */
	public CellIndex getDivisibleSites() {
		return divisibleSites;
	}

	public void incrStateCount(Cell cell) {
		Integer currentState = cell.getState();
		
		if (!stateMap.containsKey(currentState)) {
			stateMap.put(currentState, 0);
		}
		
		Integer currentCount = stateMap.get(currentState);
		stateMap.put(currentState, currentCount+1);
	}

	public void decrStateCount(Cell cell) {
		Integer currentState = cell.getState();
		Integer currentCount = stateMap.get(currentState);
		stateMap.put(currentState, currentCount-1);
	}

	public Map<Integer, Integer> getStateMap() {
		return stateMap;
	}
}
