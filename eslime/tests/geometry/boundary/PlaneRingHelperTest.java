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

package geometry.boundary;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.boundaries.helpers.PlaneRingHelper;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import test.EslimeTestCase;

public class PlaneRingHelperTest extends EslimeTestCase {

    private PlaneRingHelper helper;

    @Override
    public void setUp() {
        Lattice lattice = new RectangularLattice();
        int[] dims = new int[]{4, 4};
        helper = new PlaneRingHelper(lattice, dims);
    }

    public void testWrapLeft() {
        Coordinate initial, actual, expected;

        initial = new Coordinate(-1, 1, 0);
        expected = new Coordinate(3, 1, Flags.BOUNDARY_APPLIED);
        actual = helper.wrap(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(-5, 2, 0);
        expected = new Coordinate(3, 2, Flags.BOUNDARY_APPLIED);
        actual = helper.wrap(initial);
        assertEquals(expected, actual);
    }

    public void testWrapRight() {
        Coordinate initial, actual, expected;

        initial = new Coordinate(4, 1, 0);
        expected = new Coordinate(0, 1, Flags.BOUNDARY_APPLIED);
        actual = helper.wrap(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(8, 2, 0);
        expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED);
        actual = helper.wrap(initial);
        assertEquals(expected, actual);
    }

    public void testReflectAbove() {
        Coordinate initial, actual, expected;

        initial = new Coordinate(2, 4, 0);
        expected = new Coordinate(2, 3, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(3, 5, 0);
        expected = new Coordinate(3, 2, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(0, 6, 0);
        expected = new Coordinate(0, 1, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(1, 8, 0);
        expected = new Coordinate(1, 0, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);
    }

    public void testReflectBelow() {
        Coordinate initial, actual, expected;

        initial = new Coordinate(2, -1, 0);
        expected = new Coordinate(2, 0, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(3, -2, 0);
        expected = new Coordinate(3, 1, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(3, -4, 0);
        expected = new Coordinate(3, 3, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);

        initial = new Coordinate(3, -5, 0);
        expected = new Coordinate(3, 3, Flags.BOUNDARY_APPLIED);
        actual = helper.reflect(initial);
        assertEquals(expected, actual);
    }


}
