/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class ConstantIntegerTest extends ArgumentTest {
    protected double[] results;
    private static final int VALUE = 7;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConstantInteger query = new ConstantInteger(VALUE);

        results = new double[NUM_ITERATES];
        for (int i = 0; i < NUM_ITERATES; i++) {
            results[i] = query.next();
        }
    }

    public void testMean() throws Exception {

        double expected = VALUE;
        double actual = mean(results);

        assertEquals(expected, actual, epsilon);
    }

}
