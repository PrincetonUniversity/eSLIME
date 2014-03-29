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

package geometry.lattice;

import structural.identifiers.Coordinate;
import structural.identifiers.Flags;
import test.EslimeTestCase;

public class TriangularLatticeTest extends EslimeTestCase {

    private Lattice lattice;
    private Coordinate o, p, q, r, s, t;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        lattice = new TriangularLattice();

        // Origin
        o = new Coordinate(0, 0, 0);

        // +u
        p = new Coordinate(3, 0, 0);

        // +v
        q = new Coordinate(3, 3, 0);

        // +w
        r = new Coordinate(0, 3, 0);

        // +u +v
        s = new Coordinate(6, 3, 0);

        // +u +v -w
        t = new Coordinate(6, 6, 0);
    }

    public void testDimensionality() {
        assertEquals(2, lattice.getDimensionality());
    }

    public void testConnectivity() {
        assertEquals(3, lattice.getConnectivity());
    }

    public void testAdjust() {
        Coordinate initial, actual, expected;

        // The origin should be unaffected
        initial = new Coordinate(0, 0, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(0, 0, 0);
        assertEquals(actual, expected);

        // Y offset should be unaffected
        initial = new Coordinate(0, 8, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(0, 8, 0);
        assertEquals(actual, expected);

        // X-offset should get an adjustment to Y
        initial = new Coordinate(8, 0, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(8, 4, 0);
        assertEquals(actual, expected);

        // (-1, 0) --> (-1, -1)
        initial = new Coordinate(-1, 0, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(-1, -1, 0);
        assertEquals(expected, actual);

        // (-2, 0) --> (-2, -1)
        initial = new Coordinate(-2, 0, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(-2, -1, 0);
        assertEquals(expected, actual);

        // (-3, 0) --> (-3, -2)
        initial = new Coordinate(-3, 0, 0);
        actual = lattice.adjust(initial);
        expected = new Coordinate(-3, -2, 0);
        assertEquals(expected, actual);
    }

    public void testBasis() {
        Coordinate[] basis = lattice.getBasis();

        assertEquals(basis.length, 3);

        Coordinate southeast = new Coordinate(1, 0, 0);
        Coordinate northeast = new Coordinate(1, 1, 0);
        Coordinate north = new Coordinate(0, 1, 0);

        assertEquals(basis[0], southeast);
        assertEquals(basis[1], northeast);
        assertEquals(basis[2], north);
    }

    public void testGetAnnulus() {
        Coordinate[] actual, expected;
        Coordinate origin = new Coordinate(0, 0, 0);

        // r=0
        actual = lattice.getAnnulus(origin, 0);
        expected = new Coordinate[]{origin};
        assertArraysEqual(actual, expected, true);

        // r=1
        actual = lattice.getAnnulus(origin, 1);
        expected = new Coordinate[]{new Coordinate(0, 1, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(0, -1, 0),
                new Coordinate(-1, -1, 0),
                new Coordinate(-1, 0, 0)
        };

        assertArraysEqual(actual, expected, true);

        // r=2
        actual = lattice.getAnnulus(origin, 2);
        expected = new Coordinate[]{new Coordinate(0, 2, 0),
                new Coordinate(1, 2, 0),
                new Coordinate(2, 2, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(2, 0, 0),
                new Coordinate(1, -1, 0),
                new Coordinate(0, -2, 0),
                new Coordinate(-1, -2, 0),
                new Coordinate(-2, -2, 0),
                new Coordinate(-2, -1, 0),
                new Coordinate(-2, 0, 0),
                new Coordinate(-1, 1, 0)
        };

        assertArraysEqual(actual, expected, true);
    }

    public void testGetNeighbors() {
        Coordinate[] actual, expected;
        Coordinate origin = new Coordinate(0, 0, 0);

        // Should be same as r=1 annulus
        actual = lattice.getNeighbors(origin);
        expected = new Coordinate[]{new Coordinate(0, 1, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(0, -1, 0),
                new Coordinate(-1, -1, 0),
                new Coordinate(-1, 0, 0)
        };

        assertArraysEqual(expected, actual, true);
    }

    public void testGetDisplacement() {
        Coordinate expected, actual;

        // +u
        actual = lattice.getDisplacement(o, p);
        expected = new Coordinate(3, 0, 0, Flags.VECTOR);
        assertEquals(actual, expected);

        // +v
        actual = lattice.getDisplacement(o, q);
        expected = new Coordinate(0, 3, 0, Flags.VECTOR);
        assertEquals(actual, expected);

        // +w
        actual = lattice.getDisplacement(o, r);
        expected = new Coordinate(0, 0, 3, Flags.VECTOR);
        assertEquals(actual, expected);

        // +u +v
        actual = lattice.getDisplacement(o, s);
        expected = new Coordinate(3, 3, 0, Flags.VECTOR);
        assertEquals(actual, expected);

        // +u +v +w�= +2v (tricky!)
        actual = lattice.getDisplacement(o, t);
        expected = new Coordinate(0, 6, 0, Flags.VECTOR);
        assertEquals(actual, expected);

    }

    public void testGetRel2Abs() {
        Coordinate actual, expected;

        Coordinate op, oq, or, os, ot;
        op = new Coordinate(3, 0, 0, Flags.VECTOR);
        oq = new Coordinate(0, 3, 0, Flags.VECTOR);
        or = new Coordinate(0, 0, 3, Flags.VECTOR);
        os = new Coordinate(3, 3, 0, Flags.VECTOR);
        ot = new Coordinate(3, 3, 3, Flags.VECTOR);


        actual = lattice.rel2abs(o, op);
        expected = p;
        assertEquals(expected, actual);

        actual = lattice.rel2abs(o, oq);
        expected = q;
        assertEquals(expected, actual);

        actual = lattice.rel2abs(o, or);
        expected = r;
        assertEquals(expected, actual);

        actual = lattice.rel2abs(o, os);
        expected = s;
        assertEquals(expected, actual);

        actual = lattice.rel2abs(o, ot);
        expected = t;
        assertEquals(expected, actual);
    }

    public void testGetL1Distance() {
        int expected, actual;

        // +u
        actual = lattice.getL1Distance(o, p);
        expected = 3;
        assertEquals(expected, actual);

        // +v
        actual = lattice.getL1Distance(o, q);
        expected = 3;
        assertEquals(expected, actual);

        // +w
        actual = lattice.getL1Distance(o, r);
        expected = 3;
        assertEquals(expected, actual);

        // +u +v
        actual = lattice.getL1Distance(o, s);
        expected = 6;
        assertEquals(expected, actual);

        // +u +v +w = +2v (tricky!)
        actual = lattice.getL1Distance(o, t);
        expected = 6;
        assertEquals(expected, actual);
    }

    public void testOrthoDisplacement() {
        Coordinate expected, actual;

        // +u
        actual = lattice.getOrthoDisplacement(o, p);
        expected = new Coordinate(3, 0, 0, Flags.VECTOR);
        assertEquals(actual, expected);

        // +v
        actual = lattice.getOrthoDisplacement(o, q);
        expected = new Coordinate(3, 0, 3, Flags.VECTOR);
        assertEquals(actual, expected);

        // +w
        actual = lattice.getOrthoDisplacement(o, r);
        expected = new Coordinate(0, 0, 3, Flags.VECTOR);
        assertEquals(actual, expected);

        // +u +v
        actual = lattice.getOrthoDisplacement(o, s);
        expected = new Coordinate(6, 0, 3, Flags.VECTOR);
        assertEquals(actual, expected);

        // +u +v -w
        actual = lattice.getOrthoDisplacement(o, t);
        expected = new Coordinate(6, 0, 6, Flags.VECTOR);
        assertEquals(expected, actual);
    }

    public void testInvAdjust() {
        Coordinate initial, actual, expected;

        // The origin should be unaffected
        initial = new Coordinate(0, 0, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(0, 0, 0);
        assertEquals(actual, expected);

        // Y offset should be unaffected
        initial = new Coordinate(0, 8, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(0, 8, 0);
        assertEquals(actual, expected);

        // X-offset should get an adjustment to Y
        initial = new Coordinate(8, 4, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(8, 0, 0);
        assertEquals(actual, expected);

        // (-1, 0) <-- (-1, -1)
        initial = new Coordinate(-1, -1, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(-1, 0, 0);
        assertEquals(expected, actual);

        // (-2, 0) <-- (-2, -1)
        initial = new Coordinate(-2, -1, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(-2, 0, 0);
        assertEquals(expected, actual);

        // (-3, 0) <-- (-3, -2)
        initial = new Coordinate(-3, -2, 0);
        actual = lattice.invAdjust(initial);
        expected = new Coordinate(-3, 0, 0);
        assertEquals(expected, actual);
    }

    public void testClone() {
        Object cloned = lattice.clone();
        assertEquals(lattice.getClass(), cloned.getClass());
        assertFalse(lattice == cloned);
    }
}
