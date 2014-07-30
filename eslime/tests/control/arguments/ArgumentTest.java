/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public abstract class ArgumentTest extends EslimeTestCase {

    protected final static int NUM_ITERATES = 100;

    // Statistics impl copied from http://introcs.cs.princeton.edu/java/stdlib/StdStats.java.html
    // I should really set up Apache commons math, but to do that I have to figure out Maven, which
    // I'm putting off.
    /**
     * Returns the average value in the array a[], NaN if no such value.
     */
    protected static double mean(double[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        return sum / a.length;
    }

    /**
     * Returns the sample variance in the array a[], NaN if no such value.
     */
    protected static double var(double[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
}
