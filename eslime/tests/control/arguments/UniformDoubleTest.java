/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class UniformDoubleTest extends ArgumentTest {

    private double min = 1.0;
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
        double expected = 4.5;
        double actual = mean(results);

        double var = (1.0 / 12.0) * Math.pow(max - min, 2.0);
        assertEquals(expected, actual, var);
    }

}
