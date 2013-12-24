package jeslime.geometry.boundary;

import geometry.boundaries.Absorbing;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import jeslime.EslimeTestCase;
import structural.Flags;
import structural.identifiers.Coordinate;

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
}