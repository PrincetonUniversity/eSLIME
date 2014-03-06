/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package structural;

import java.util.HashMap;

/**
 * Created by dbborens on 3/5/14.
 */
public class Chooser<T> {

    private HashMap<T, Double> weights;
    private boolean closed;
    private double totalWeight = 0.0;

    public Chooser() {
        weights = new HashMap<>();
        closed = false;
    }

    public void add(T token, double weight) {
        if (closed) {
            throw new IllegalStateException("Attempted to write to finalized chooser.");
        }

        weights.put(token, weight);
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

        double[] upperBounds = upperBoundArray(keys);

        Integer target = findKey(x, upperBounds);

        T ret = (T) keys[target];
        return ret;
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
            return binaryRangeSearch(midpoint + 1, upper, x, bins);

            // Recursive case 2: Midpoint is too high; check lower half.
        } else if (x < min) {
            return binaryRangeSearch(lower, midpoint - 1, x, bins);

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
        if (!(obj instanceof Chooser)) {
            return false;
        }

        Chooser other = (Chooser) obj;

        if (!EpsilonUtil.epsilonEquals(totalWeight, other.totalWeight)) {
            return false;
        }

        if (other.weights.size() != this.weights.size()) {
            return false;
        }

        for (T key : weights.keySet()) {
            if (!other.weights.containsKey(key)) {
                return false;
            }

            double p = weights.get(key);
            double q = (double) other.weights.get(key);

            if (!EpsilonUtil.epsilonEquals(p, q)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Chooser<T> clone() {
        if (!closed) {
            throw new IllegalStateException("Cannot clone chooser until it is closed.");
        }

        Chooser<T> cloned = new Chooser<T>();

        for (T key : weights.keySet()) {
            cloned.add(key, weights.get(key));
        }

        cloned.close();

        return cloned;
    }
}
