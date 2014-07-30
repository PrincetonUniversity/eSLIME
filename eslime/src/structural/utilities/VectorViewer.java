/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural.utilities;

import no.uib.cipr.matrix.Vector;

@Deprecated
public class VectorViewer {

    protected Vector v;

    private double max;

    private double min;

    private double range;

    public VectorViewer(Vector v, double min, double max) {
        this.v = v;
        this.min = min;
        this.max = max;
        range = max - min;
    }

    public double max() {
        return max;
    }

    public double min() {
        return min;
    }

    public double get(int i) {
        return v.get(i);
    }

    public int size() {
        return v.size();
    }

    /**
     * Returns a value scaled from 0 to 1, with 0 corresponding
     * to the minimum value of the vector and 1 corresponding to the
     * maximum.
     *
     * @param i Index into the vector.
     */
    public double getScaled(int i) {
        if (range == 0)
            return 0;

        // If we are using non-zero extrema, return zeros as zeros; otherwise, normalize
        if (min != 0 && v.get(i) == 0d)
            return 0d;

        if (v.get(i) < min)
            throw new RuntimeException(v.get(i) + " < " + min + " @ " + i);

        double x = (v.get(i) - min) / range;
        return x;
    }

    public double[] getData() {
        double[] data = new double[v.size()];
        for (int i = 0; i < v.size(); i++) {
            data[i] = v.get(i);
        }

        return data;
    }
}

