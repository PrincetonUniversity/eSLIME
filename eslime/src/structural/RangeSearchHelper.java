/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 3/19/14.
 */
public class RangeSearchHelper {
    private ArrayList<Double> bins;

    public RangeSearchHelper(ArrayList<Double> bins) {
        this.bins = bins;
    }

    // Recursive binary range search
    private int binaryRangeSearch(int lower, int upper, double x) {
        // Find midpoint of current range, rounded down.
        int midpoint = (lower + upper) / 2;

        // Set the bounds of the bucket.
        double max = bins.get(midpoint);
        double min;

        // Failure case: we didn't find it.
        if (upper < lower) {
            return -1;
        }

        if (midpoint == 0) {
            min = 0.0;
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

    public int findKey(double x) {
        int lower = 0;

        // -1 because bins.size() is one larger than its largest index, and
        // -1 because bins() contains one too many elements (since we put a
        // dummy element in at the beginning)
        int upper = bins.size() - 1;

        // Find the desired key using a binary range search
        int bin = binaryRangeSearch(lower, upper, x);

        // Bin indices are one higher than key indices, so reduce by one.
        return bin - 1;
    }

}
