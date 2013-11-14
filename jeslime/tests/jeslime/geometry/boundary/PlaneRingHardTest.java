package jeslime.geometry.boundary;

import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingHard;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import jeslime.EslimeTestCase;

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
}
