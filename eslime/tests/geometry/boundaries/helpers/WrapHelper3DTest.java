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

package geometry.boundaries.helpers;

import control.identifiers.Coordinate;
import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import geometry.shape.Cuboid;
import geometry.shape.Shape;
import junit.framework.TestCase;

public class WrapHelper3DTest extends TestCase {


    private WrapHelper3D query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new CubicLattice();
        Shape shape = new Cuboid(lattice, 5, 5, 5);
        query = new WrapHelper3D(shape, lattice);
    }

    public void testWrapAll() throws Exception {
        Coordinate toWrap = new Coordinate(5, 5, 5, 0);
        Coordinate expected = new Coordinate(0, 0, 0, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

    public void testXWrap() throws Exception {
        // Positive out of bounds X coordinate
        Coordinate toWrap = new Coordinate(6, 0, 0, 0);
        Coordinate expected = new Coordinate(1, 0, 0, 0);
        Coordinate actual = query.xWrap(toWrap);
        assertEquals(expected, actual);

        // Negative out of bounds X coordinate
        toWrap = new Coordinate(-1, 0, 0, 0);
        expected = new Coordinate(4, 0, 0, 0);
        actual = query.xWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testYWrap() throws Exception {
        // Positive out of bounds Y coordinate
        Coordinate toWrap = new Coordinate(0, 6, 0, 0);
        Coordinate expected = new Coordinate(0, 1, 0, 0);
        Coordinate actual = query.yWrap(toWrap);
        assertEquals(expected, actual);

        // Negative out of bounds Y coordinate
        toWrap = new Coordinate(0, -1, 0, 0);
        expected = new Coordinate(0, 4, 0, 0);
        actual = query.yWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testZWrap() throws Exception {
        // Positive out of bounds Z coordinate
        Coordinate toWrap = new Coordinate(0, 0, 6, 0);
        Coordinate expected = new Coordinate(0, 0, 1, 0);
        Coordinate actual = query.zWrap(toWrap);
        assertEquals(expected, actual);

        // Negative out of bounds Z coordinate
        toWrap = new Coordinate(0, 0, -1, 0);
        expected = new Coordinate(0, 0, 4, 0);
        actual = query.zWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testAllInBounds() throws Exception {
        Coordinate toWrap = new Coordinate(3, 3, 3, 0);
        Coordinate expected = new Coordinate(3, 3, 3, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }
}