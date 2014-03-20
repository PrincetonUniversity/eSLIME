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

package processes;

import structural.identifiers.Coordinate;
import test.EslimeTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by dbborens on 3/7/14.
 */
public class MaxTargetHelperTest extends EslimeTestCase {
    // Set of all outcomes
    private Coordinate[] omega;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        omega = new Coordinate[10];
        for (int i = 0; i < 10; i++) {
            omega[i] = new Coordinate(i, 0, 0);
        }

        random = new Random(RANDOM_SEED);
    }

    public void testOverMaximum() throws Exception {
        doTest(1, 1);
    }

    public void testUnderMaximum() throws Exception {
        doTest(15, 10);
    }

    public void testNoMaximum() throws Exception {
        doTest(-1, 10);
    }

    public void testTrivial() throws Exception {
        doTest(0, 0);
    }

    public void testListCase() {
        List<Coordinate> omegaList = Arrays.asList(omega);
        Coordinate[] coordinates = MaxTargetHelper.respectMaxTargets(omegaList, 1, random);
        int actual = coordinates.length;
        int expected = 1;

        assertEquals(expected, actual);
    }

    private void doTest(int maxTargetsArg, int expected) {
        Coordinate[] actualArr = MaxTargetHelper.respectMaxTargets(omega, maxTargetsArg, random);
        int actual = actualArr.length;
        assertEquals(expected, actual);
    }
}
