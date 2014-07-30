/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class ConstantDoubleTest extends ArgumentTest {
    private double value = 12e-5;
    protected double[] results;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConstantDouble query = new ConstantDouble(value);

        results = new double[NUM_ITERATES];
        for (int i = 0; i < NUM_ITERATES; i++) {
            results[i] = query.next();
        }
    }

    public void testMean() throws Exception {

        double expected = value;
        double actual = mean(results);

        assertEquals(expected, actual, epsilon);
    }

}
