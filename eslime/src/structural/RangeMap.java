/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package structural;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A weighted collection of items of class T. Each T has some weight associated
 * with it. The total weight of the system is the sum of these weights. The
 * order of the items is identical to that in which they were loaded.
 *
 * For example, I might load Foo with weight 0.5, and Bar with weight 1.0. That
 * would mean that weight range 0 <= weight < 0.5 corresponds to foo, and
 * 0.5 <= weight < 1.5 corresponds to bar.
 *
 * After the collection is declared final (using the close() method), the user
 * may sample the collection by invoking the selectTarget(...) method, which
 * will retrieve items by range in the manner described above.
 *
 * One would like
 */
public class RangeMap<T> {

    private HashMap<T, Double> weights;
    
    // We track the floor of each element, rather than counting on the key
    // set of weights, so that we can compare across RangeMaps by preserving
    // order. The first element is pre-loaded as 0.0, but each ceiling is then
    // appended. This means that bins has one too many elements, which is
    // handled accordingly.
    private ArrayList<Double> bins;
    
    private boolean closed;
    private double totalWeight = 0.0;

    public RangeMap() {
        weights = new HashMap<>();
        bins = new ArrayList<>();
        bins.add(0.0);
        closed = false;
    }

    public void add(T token, double weight) {
        if (closed) {
            throw new IllegalStateException("Attempted to write to finalized chooser.");
        }

        appendBin(weight);
        weights.put(token, weight);
        
    }

    private void appendBin(double weight) {
        int n = bins.size();
        double lastBin = bins.get(n - 1);
        bins.add(lastBin + weight);
    }

    public void close() {
        if (closed) {
            throw new IllegalStateException("Attempted to close state, but it was already closed.");
        }

        closed = true;

        for (double weight : weights.values()) {
            totalWeight += weight;
        }
    }

    /**
     * Returns a particular process based on an input between 0
     * and the total weight, with processes sorted numerically
     * by ID number.
     * <p/>
     * So if w_i is the weight of process i, then you'll get back
     * <p/>
     * 0 <= x < w_1         --> return process 1's id
     * w_1 <= x < w_1 + w_2 --> return process 2's id
     * ...
     * w_n-1 <= x < sum(w)  --> return process n's id
     *
     * @return
     */
    public T selectTarget(double x) {
        if (!closed) {
            throw new IllegalStateException("Selection sought before chooser was closed. Before choosing a selection from a chooser, you must signal that all selections are loaded by calling close().");
        }

        Object[] keys = weights.keySet().toArray(new Object[0]);

        Integer target = findKey(x);

        T ret = (T) keys[target];
        return ret;
    }

    private int findKey(double x) {
        int lower = 0;

        // -1 because bins.size() is one larger than its largest index, and
        // -1 because bins() contains one too many elements (since we put a
        // dummy element in at the beginning)
        int upper = bins.size() - 1 - 1;

        // Find the desired key using a binary range search
        return binaryRangeSearch(lower, upper, x);
    }

    // Recursive binary range search -- public exposure for testing
    public int binaryRangeSearch(int lower, int upper, double x) {
        // Find midpoint of current range, rounded down.
        int midpoint = (lower + upper) / 2;

        // Set the bounds of the bucket.
        double max = bins.get(midpoint);
        double min;

        // Failure case: we didn't find it.
        if (upper < lower) {
            return -1;
        }

        // Lower bound must be 0.
        if (midpoint == 0) {
            min = 0D;
        } else {
            min = bins.get(midpoint - 1);
        }

        // Recursive case 1: Midpoint is too low; check upper half.
        if (x >= max) {
            return binaryRangeSearch(midpoint + 1, upper, x);

            // Recursive case 2: Midpoint is too high; check lower half.
        } else if (x < min) {
            return binaryRangeSearch(lower, midpoint - 1, x);

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
    private double[] upperBoundArray(Object[] keys) {
        double[] upperBounds = new double[weights.size()];
        double total = 0D;
        int i = 0;
        for (Object obj : keys) {
            // This object/T silliness is a quirk of Java arrays.
            T key = (T) obj;
            total += weights.get(key);
            upperBounds[i] = total;
            i++;
        }

        return upperBounds;
    }

    public double getTotalWeight() {
        if (!closed) {
            throw new IllegalStateException("Chooser must be closed before checking total weight.");
        }

        return totalWeight;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RangeMap)) {
            return false;
        }

        RangeMap other = (RangeMap) obj;

        if (!EpsilonUtil.epsilonEquals(totalWeight, other.totalWeight)) {
            return false;
        }


        if (!contentsEqual(this, other)) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if each weight range in each map returns equal objects for
     * the same value of x.
     *
     * One would like to use a simpler method of comparing maps, say by asking
     * whether each key is contained in both maps. However, as
     * this class is generic, one is instead forced to compare element by
     * element.
     *
     * @return
     */
    private boolean contentsEqual(RangeMap p, RangeMap q) {
        // If they have a different number of elements, we already know that 
        // they are unequal.
        if (p.weights.size() != q.weights.size()) {
            return false;
        }

        // Test each subsequent bin by its midpoint value.
        // The -1 is because the variable bins ends with the last ceiling added,
        // but it is supposed to be a list of floors.
        for (int i = 0; i < bins.size() - 1; i++) {
            double range = bins.get(i+1) - bins.get(i);
            double floor = bins.get(i);
            double midpoint = floor + (range / 2.0);

            if (!binsEqual(p, q, midpoint)) {
                return false;
            }
        }

        return true;
    }

    private boolean binsEqual(RangeMap p, RangeMap q, double midpoint) {
        Object pResult = p.selectTarget(midpoint);
        Object qResult = q.selectTarget(midpoint);

        if (!pResult.equals(qResult)) {
            return false;
        }
       
        return true;
    }

    @Override
    public RangeMap<T> clone() {
        if (!closed) {
            throw new IllegalStateException("Cannot clone range map until it is closed.");
        }

        RangeMap<T> cloned = new RangeMap<T>();

        for (T key : weights.keySet()) {
            cloned.add(key, weights.get(key));
        }

        cloned.close();

        return cloned;
    }
}
