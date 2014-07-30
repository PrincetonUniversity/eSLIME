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
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingReflecting;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import test.EslimeTestCase;

public class PlaneRingReflectingTest extends EslimeTestCase {
    private Boundary rect;
    private Boundary tri;

    private Lattice rectLattice;
    private Lattice triLattice;

    private Shape rectShape;
    private Shape triShape;

    public void setUp() {
        rectLattice = new RectangularLattice();
        triLattice = new TriangularLattice();

        rectShape = new Rectangle(rectLattice, 6, 4);
        triShape = new Rectangle(triLattice, 6, 4);

        rect = new PlaneRingReflecting(rectShape, rectLattice);
        tri = new PlaneRingReflecting(triShape, triLattice);
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
        q = new Coordinate(6, 2, 0);

        Coordinate expected, actual;

        // Rectangular
        expected = new Coordinate(5, 1, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(q);
        assertEquals(expected, actual);

        // Triangular
        expected = new Coordinate(5, 4, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(q);
        assertEquals(expected, actual);
    }

    public void testApplyOutsideY() {
        Coordinate p, q;
        p = new Coordinate(0, 4, 0);
        q = new Coordinate(2, -1, 0);

        Coordinate expected, actual;

        // Rectangular
        expected = new Coordinate(0, 3, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(2, 0, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(q);
        assertEquals(expected, actual);

        // Triangular
        expected = new Coordinate(0, 3, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(2, 2, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(q);
        assertEquals(expected, actual);
    }

    public void testApplyRectXY() {
        Coordinate p, q;
        p = new Coordinate(-1, 4, 0);
        q = new Coordinate(6, -1, 0);

        Coordinate actual, expected;

        expected = new Coordinate(5, 3, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        actual = rect.apply(q);
        assertEquals(expected, actual);

    }

    public void testApplyTriXY() {
        Coordinate p, q;
        p = new Coordinate(-1, 3, 0);
        q = new Coordinate(6, 2, 0);

        Coordinate actual, expected;

        expected = new Coordinate(5, 5, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        actual = tri.apply(q);
        assertEquals(expected, actual);
    }

    /**
     * Integration test verifying that correct neighbors are being
     * reported. Created for bug report 62997644.
     */
    public void testBoundaryNeighbors() {
        Geometry geom = new Geometry(rectLattice, rectShape, rect);

        Coordinate[] expected, actual;
        Coordinate query;

        Coordinate bottom = new Coordinate(2, 0, 0);
        Coordinate top = new Coordinate(2, 3, 0);
        Coordinate leftmost = new Coordinate(0, 2, 0);
        Coordinate rightmost = new Coordinate(5, 2, 0);

        // Test bottom position
        query = bottom;
        expected = new Coordinate[]{
                new Coordinate(1, 0, 0),
                new Coordinate(3, 0, 0),
                new Coordinate(2, 0, Flags.BOUNDARY_APPLIED),  // Reflect up
                new Coordinate(2, 1, 0)
        };
        actual = geom.getNeighbors(query, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(expected, actual, true);

        // Test top position
        query = top;
        expected = new Coordinate[]{
                new Coordinate(1, 3, 0),
                new Coordinate(3, 3, 0),
                new Coordinate(2, 3, Flags.BOUNDARY_APPLIED),  // Reflect down
                new Coordinate(2, 2, 0)
        };
        actual = geom.getNeighbors(query, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(expected, actual, true);

        // Test leftmost position
        query = leftmost;
        expected = new Coordinate[]{
                new Coordinate(5, 2, Flags.BOUNDARY_APPLIED),  // Wrap to right
                new Coordinate(1, 2, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 3, 0)
        };
        actual = geom.getNeighbors(query, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(expected, actual, true);

        // Test rightmost position
        query = rightmost;
        expected = new Coordinate[]{
                new Coordinate(4, 2, 0),
                new Coordinate(0, 2, Flags.BOUNDARY_APPLIED),  // Wrap to left
                new Coordinate(5, 3, 0),
                new Coordinate(5, 1, 0)
        };
        actual = geom.getNeighbors(query, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(expected, actual, true);

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
