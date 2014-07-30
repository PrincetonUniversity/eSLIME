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

package processes;

import com.sun.deploy.util.ArrayUtil;
import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.UniformInteger;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.gillespie.GillespieState;
import test.EslimeTestCase;

/**
 * Test to make sure that stochastic values work as expected in a process.
 * This is a regression test for a possible failure mode observed in April 2014.
 *
 * Created by dbborens on 5/1/14.
 */
public class StochasticProcessTest extends EslimeTestCase {

    public void testOngoing() throws Exception {
        double[] obs = new double[100];
        GeneralParameters p = makeMockGeneralParameters();
        UniformInteger ongoing = new UniformInteger(408, 410, p.getRandom());
        StochasticProcess query = new StochasticProcess(ongoing);
        for (int i = 0; i < 100; i++) {
            query.target(null);
            obs[i] = query.getOngoingVal();
        }

        examine(obs);

    }
    private class StochasticProcess extends Process {

        private Argument<Integer> ongoing;
        private Integer ongoingVal;

        public StochasticProcess(Argument<Integer> ongoing) {
            super(null, null, null, 0);
            this.ongoing = ongoing;
        }


        @Override
        protected String getProcessClass() {
            return null;
        }

        @Override
        public void target(GillespieState gs) throws HaltCondition {
            ongoingVal = ongoing.next();
        }

        public double getOngoingVal() {
            return ongoingVal;
        }

        @Override
        public void fire(StepState state) throws HaltCondition {

        }

    }

    private void examine(double[] results) {
        assertEquals(408.0, minValue(results), epsilon);
        assertEquals(409.0, maxValue(results), epsilon);
        double expected = 408.5;
        double actual = mean(results);
        double var = (1.0 / 12.0) * Math.pow(1, 2.0);
        assertEquals(expected, actual, var);
    }

    protected static double mean(double[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        return sum / a.length;
    }

    private double minValue(double[] a) {
        double cur = Double.MAX_VALUE;

        for (double x : a) {
            if (x < cur) {
                cur = x;
            }
        }

        return cur;
    }

    private double maxValue(double[] a) {
        double cur = Double.MIN_VALUE;

        for (double x : a) {
            if (x > cur) {
                cur = x;
            }
        }

        return cur;
    }

}
