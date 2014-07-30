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

package structural;

import test.EslimeTestCase;

/**
 * Created by dbborens on 3/5/14.
 */
public class RangeMapTest extends EslimeTestCase {
    private RangeMap<Integer> query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new RangeMap<>();
    }

    public void testAdd() throws Exception {
        // Verify that rangemap is empty.
        assertEquals(0.0, query.getTotalWeight(), epsilon);
        assertEquals(0, query.getNumBins());

        // Add an item.
        query.add(1, 0.5);

        // Verify that the total weight is the weight of the item added.
        assertEquals(0.5, query.getTotalWeight(), epsilon);

        // Verify that there is one item in the range map.
        assertEquals(1, query.getNumBins());
    }

    public void testSelectTarget() throws Exception {
        setupThreeElementCase();
        Integer actual, expected;

        // First bin
        expected = 5;
        actual = query.selectTarget(0.25);
        assertEquals(expected, actual);

        // Second bin
        expected = -3;
        actual = query.selectTarget(1.0);
        assertEquals(expected, actual);

        // Third bin
        expected = 0;
        actual = query.selectTarget(1.75);
        assertEquals(expected, actual);
    }

    public void testLowerBoundInclusivity() throws Exception {
        setupThreeElementCase();
        Integer actual, expected;

        // First bin
        expected = 5;
        actual = query.selectTarget(0.0);
        assertEquals(expected, actual);

        // Second bin
        expected = -3;
        actual = query.selectTarget(0.5);
        assertEquals(expected, actual);

        // Third bin
        expected = 0;
        actual = query.selectTarget(1.5);
        assertEquals(expected, actual);
    }

    public void testGetTotalWeight() throws Exception {
        setupThreeElementCase();
        assertEquals(2.0, query.getTotalWeight(), epsilon);
    }

    /**
     * Make sure that, when two range maps have members of
     * unequal classes, they are not themselves considered
     * equal.
     *
     * @throws Exception
     */
    public void testInequalityClassCase() throws Exception {
        setupThreeElementCase();
        RangeMap<String> other = new RangeMap<>();
        other.add("Due to type erasure, there is no way to check equality until at least one element is loaded. This may have been fixed in Java 8.", 1.0);
        assertNotEquals(query, other);
    }

    /**
     * Make sure that, when two range maps have members of
     * the same class with unequal values, they are not
     * considered equal.
     *
     * @throws Exception
     */
    public void testInequalityValueCase() throws Exception {
        setupThreeElementCase();
        RangeMap<Integer> other = new RangeMap<>();
        other.add(1, 0.5);      // Different
        other.add(-3, 1.0);     // Same
        other.add(0, 0.5);      // Same

        assertNotEquals(query, other);
    }

    /**
     * Make sure that, when two range maps have members of
     * the same class with equal values, but they have
     * different weights, they are not considered equal.
     *
     * @throws Exception
     */
    public void testInequalityWeightsCase() throws Exception {
        setupThreeElementCase();
        RangeMap<Integer> other = new RangeMap<>();
        other.add(5, 0.25);      // Different
        other.add(-3, 1.0);     // Same
        other.add(0, 0.5);      // Same

        assertNotEquals(query, other);
    }

    /**
     * Make sure that, when two range maps have members of
     * the same class with equal values, but in the wrong
     * order, they are not considered equal.
     *
     * @throws Exception
     */
    public void testInequalityOrderCase() throws Exception {
        setupThreeElementCase();
        RangeMap<Integer> other = new RangeMap<>();
        other.add(-3, 1.0);     // Used to be second
        other.add(5, 0.5);      // Used to be first
        other.add(0, 0.5);      // Same

        assertNotEquals(query, other);
    }

    /**
     * Make sure that, if two range maps have the same ranges
     * in the same order pointing to equal items, they are
     * considered equal.
     *
     * @throws Exception
     */
    public void testEquals() throws Exception {
        setupThreeElementCase();
        RangeMap<Integer> other = new RangeMap<>();
        other.add(5, 0.5);      // Same
        other.add(-3, 1.0);     // Same
        other.add(0, 0.5);      // Same

        assertEquals(query, other);
    }

    public void testClone() throws Exception {
        RangeMap<Integer> clone = query.clone();
        assertEquals(query, clone);
    }

    private void setupThreeElementCase() {
        query.add(5, 0.5);
        query.add(-3, 1.0);
        query.add(0, 0.5);
    }

}

