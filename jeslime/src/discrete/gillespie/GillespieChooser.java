package discrete.gillespie;

import java.util.Arrays;

/**
 * 
 * @tested GillespieTest.java
 * @author dbborens
 */
public class GillespieChooser {
	
	private GillespieState state;
	
	public GillespieChooser(GillespieState state) {
		this.state = state;
	}
	
	/**
	 * Returns a particular process based on an input between 0
	 * and the total weight, with processes sorted numerically
	 * by ID number.
	 * 
	 * So if w_i is the weight of process i, then you'll get back
	 * 
	 * 0 <= x < w_1         --> return process 1's id
	 * w_1 <= x < w_1 + w_2 --> return process 2's id
	 *    ...
	 * w_n-1 <= x < sum(w)  --> return process n's id 
	 * 
	 * @param weight input (usually random) used to choose a target.
	 * @return
	 */
	public Integer selectTarget(double x) {
		if (!state.isClosed()) {
			throw new IllegalStateException("Attempted to access Gillespie process state before it was ready.");
		}
		
		Integer[] eks = state.getKeys().clone();
		Arrays.sort(eks);
		
		double[] upperBounds = upperBoundArray(eks);
		
		Integer target = findKey(x, upperBounds);
		
		return eks[target];
	}
	
	private int findKey(double x, double[] bins) {
		int lower = 0;
		int upper = bins.length - 1;
		
		// Find the desired key using a binary range search
		return binaryRangeSearch(lower, upper, x, bins);
	}

	// Recursive binary range search -- public exposure for testing
	public int binaryRangeSearch(int lower, int upper, double x, double[] bins) {
		// Find midpoint of current range, rounded down.
		int midpoint = (lower + upper) / 2;
		
		// Set the bounds of the bucket.
		double max = bins[midpoint];
		double min;
		
		// Failure case: we didn't find it.
		if (upper < lower) {
			return -1;
		}
		
		// Lower bound must be 0.
		if (midpoint == 0) {
			min = 0D;
		} else {
			min = bins[midpoint - 1];
		}
		
		// Recursive case 1: Midpoint is too low; check upper half.
		if (x >= max) {
			return binaryRangeSearch(midpoint+1, upper, x, bins);
		
		// Recursive case 2: Midpoint is too high; check lower half.
		} else if (x < min) {
			return binaryRangeSearch(lower, midpoint-1, x, bins);
	
		// Base case: x is less than the maximum and greater
		// than the minimum. In that case, the midpoint bin
		// is exactly right, so return it.
		} else {
			return midpoint;
		}
	}
	
	// Construct an array of upper bounds, with indices corresponding to
	// keys. So process 0 will have a weight range of 0 to upperBounds[0],
	// and process i will have a weight range of upperBounds[i - 1] to
	// upperBounds[i].
	private double[] upperBoundArray(Integer[] eks) {
		double[] upperBounds = new double[eks.length];
		double total = 0D; 
		for (int i = 0; i < eks.length; i++) {
			Integer key = eks[i];
			total += state.getWeight(key);
			upperBounds[i] = total;
		}
		
		return upperBounds;
	}
}
