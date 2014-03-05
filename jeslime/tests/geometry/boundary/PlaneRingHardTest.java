/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package geometry.boundary;

import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingHard;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import test.EslimeTestCase;

public class PlaneRingHardTest extends EslimeTestCase {
	private Boundary rect;
	private Boundary tri;
	
	public void setUp() {
		Lattice rectLattice = new RectangularLattice();
		Lattice triLattice = new TriangularLattice();
		
		Shape rectShape = new Rectangle(rectLattice, 5, 3);
		Shape triShape = new Rectangle(triLattice, 5, 3);
		
		rect = new PlaneRingHard(rectShape, rectLattice);
		tri = new PlaneRingHard(triShape, triLattice);
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
		
		Coordinate expected, actual;

		// Rectangular
		expected = new Coordinate(4, 1, Flags.BOUNDARY_APPLIED);
		actual = rect.apply(p);
		assertEquals(expected, actual);
		
		expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED);
		actual = rect.apply(q);
		assertEquals(expected, actual);
		
		// Triangular
		expected = new Coordinate(4, 4, Flags.BOUNDARY_APPLIED);
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
		
		Coordinate actual;

		// Rectangular
		actual = rect.apply(p);
		assertNull(actual);
		
		actual = rect.apply(q);
		assertNull(actual);
		
		// Triangular
		actual = tri.apply(p);
		assertNull(actual);
		
		actual = tri.apply(q);
		assertNull(actual);
	}
	
	public void testApplyOutsideXY() {
		Coordinate p, q;
		p = new Coordinate(-1, 4, 0);
		q = new Coordinate(5, -5, 0);	
		
		Coordinate actual;

		// Rectangular
		actual = rect.apply(p);
		assertNull(actual);
		
		actual = rect.apply(q);
		assertNull(actual);
		
		// Triangular
		actual = tri.apply(p);
		assertNull(actual);
		
		actual = tri.apply(q);
		assertNull(actual);		
	}
    public void testCloneWithArguments() {
        Lattice lattice = new RectangularLattice();
        Shape singleton = new Rectangle(lattice, 1, 1);

        Boundary query  = rect.clone(singleton, lattice);

        // Boundaries are equal based on their class, not their dependencies
        assertEquals(rect, query);
        assertFalse(rect == query);
    }
}
