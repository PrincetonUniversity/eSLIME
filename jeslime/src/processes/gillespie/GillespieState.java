package processes.gillespie;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import structural.GeneralParameters;

/**
 * 
 * @tested GillespieTest.java
 * @author David Bruce Borenstein
 */
public class GillespieState {

	// Map of process ID --> weight
	private HashMap<Integer, Double> weightMap;
	
	// Map of process ID --> count
	private HashMap<Integer, Integer> countMap;
	
	private Integer[] expectedKeys;
	
	boolean closed = false;
	
	public GillespieState(Integer[] expectedKeys) {
		this.expectedKeys = expectedKeys;
		
		weightMap = new HashMap<Integer, Double>(expectedKeys.length);
		countMap = new HashMap<Integer, Integer>(expectedKeys.length);
	}
	
	public void add(Integer processId, Integer eventCount, Double weight) {
		if (closed) {
			throw new IllegalStateException("Attempted to modify Gillespie state after it was closed.");
		}
		
		if (weightMap.containsKey(processId) || countMap.containsKey(processId)) {
			throw new IllegalStateException("Repeat assignment to Gillespie state.");
		}
		
		weightMap.put(processId, weight);
		countMap.put(processId, eventCount);
	}
	
	/**
	 * Closes the state object to further additions, paving the way for
	 * choosing an event to fire. This must be called before using getters
	 * (except getKeys).
	 */
	public void close() {
		if (closed) {
			throw new IllegalStateException("Repeat call to close() in Gillespie process state.");
		}
		
		if (weightMap.keySet().size() != expectedKeys.length) {
			throw new IllegalStateException("Gillespie state consistency failure.");
		}
		
		closed = true;
	}
	
	public Integer[] getKeys() {
		return expectedKeys.clone();
	}
	
	/**
	 * Returns the total weight of all processes.
	 * 
	 * @return
	 */
	public double getTotalWeight() {
		if (!closed) {
			throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
		}
		
		double total = 0;
		for (Integer key : expectedKeys) {
			total += weightMap.get(key);
		}
		
		return total;
	}
	
	public double getWeight(Integer processId) {
		if (!closed) {
			throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
		}
		
		return weightMap.get(processId);
	}
	
	/**
	 * Returns number of unique events possible for a given
	 * process.
	 * 
	 * @param processID
	 */
	public int getEventCount(Integer processID) {
		if (!closed) {
			throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
		}
		
		return countMap.get(processID);

	}

	public boolean isClosed() {
		return closed;
	}

	public int getTotalCount() {
		if (!closed) {
			throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
		}
		
		int totalCount = 0;
		
		for (Integer key : countMap.keySet()) {
			totalCount += countMap.get(key);
		}
		
		return totalCount;
	}
	

}
