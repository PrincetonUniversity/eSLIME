/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package control.arguments;

import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class UniformDoubleTest extends ArgumentTest {

    private double min = -10.0;
    private double max = 10.0;

    protected double[] results;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Random random = new Random(super.RANDOM_SEED);
        UniformDouble query = new UniformDouble(min, max, random);

        results = new double[NUM_ITERATES];
        for (int i = 0; i < NUM_ITERATES; i++) {
            results[i] = query.next();
        }
    }

    public void testMean() throws Exception {
        double expected = 0.0;
        double actual = mean(results);

        double var = (1.0 / 12.0) * Math.pow(max - min, 2.0);
        assertEquals(expected, actual, var);
    }

}
