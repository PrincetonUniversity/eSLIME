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
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import test.EslimeTestCase;

public class AbsorbingTest extends EslimeTestCase {

    private Boundary rect;
    private Boundary tri;

    public void setUp() {
        Lattice rectLattice = new RectangularLattice();
        Lattice triLattice = new TriangularLattice();

        Shape rectShape = new Rectangle(rectLattice, 3, 5);
        Shape triShape = new Rectangle(triLattice, 3, 5);

        rect = new Absorbing(rectShape, rectLattice);
        tri = new Absorbing(triShape, triLattice);
    }

    public void testInfinite() {
        assertFalse(rect.isInfinite());
        assertFalse(tri.isInfinite());
    }

    public void testApplyInBounds() {
        // These are in bounds for both triangular and rectangular
        Coordinate a, b, c;
        a = new Coordinate(0, 0, 0);
        b = new Coordinate(1, 1, 0);
        c = new Coordinate(2, 2, 0);

        // Rectangular
        Coordinate actual, expected;
        expected = new Coordinate(0, 0, 0);
        actual = rect.apply(a);
        assertEquals(expected, actual);

        expected = new Coordinate(1, 1, 0);
        actual = rect.apply(b);
        assertEquals(expected, actual);

        expected = new Coordinate(2, 2, 0);
        actual = rect.apply(c);
        assertEquals(expected, actual);

        // Triangular
        expected = new Coordinate(0, 0, 0);
        actual = tri.apply(a);
        assertEquals(expected, actual);

        expected = new Coordinate(1, 1, 0);
        actual = tri.apply(b);
        assertEquals(expected, actual);

        expected = new Coordinate(2, 2, 0);
        actual = tri.apply(c);
        assertEquals(expected, actual);
    }

    public void testApplyOutsideX() {
        Coordinate p, q;
        p = new Coordinate(-1, 1, 0);
        q = new Coordinate(5, 2, 0);

        Coordinate actual;

        // p
        actual = rect.apply(p);
        assertNull(actual);

        actual = tri.apply(p);
        assertNull(actual);

        // q
        actual = rect.apply(q);
        assertNull(actual);

        actual = tri.apply(q);
        assertNull(actual);
    }

    public void testApplyOutsideY() {
        Coordinate p, q;
        p = new Coordinate(0, 5, 0);
        q = new Coordinate(2, -1, 0);

        Coordinate actual;

        // p
        actual = rect.apply(p);
        assertNull(actual);

        actual = tri.apply(p);
        assertNull(actual);

        // q
        actual = rect.apply(q);
        assertNull(actual);

        actual = tri.apply(q);
        assertNull(actual);
    }

    public void testApplyOutsideXY() {
        Coordinate p, q;
        p = new Coordinate(-1, 4, 0);
        q = new Coordinate(5, -5, 0);

        Coordinate actual;

        // p
        actual = rect.apply(p);
        assertNull(actual);

        actual = tri.apply(p);
        assertNull(actual);

        // q
        actual = rect.apply(q);
        assertNull(actual);

        actual = tri.apply(q);
        assertNull(actual);
    }

    public void testCloneWithArguments() {
        Lattice lattice = new RectangularLattice();
        Shape singleton = new Rectangle(lattice, 1, 1);

        Boundary query = rect.clone(singleton, lattice);

        // Boundaries are equal based on their class, not their dependencies
        assertEquals(rect, query);
        assertFalse(rect == query);
    }
}