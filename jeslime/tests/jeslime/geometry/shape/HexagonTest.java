package jeslime.geometry.shape;

import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Cuboid;
import geometry.shape.Hexagon;
import structural.Flags;
import structural.identifiers.Coordinate;
import jeslime.EslimeTestCase;

public class HexagonTest extends EslimeTestCase {
	
	private Hexagon hex;
	
	@Override
	public void setUp() {
		Lattice lattice = new TriangularLattice();
		
		hex = new Hexagon(lattice, 2);
	}
	
	public void testGetCenter() {
		Coordinate actual, expected;
		expected = new Coordinate(2, 3, 0);
		actual = hex.getCenter();
		assertEquals(expected, actual);
	}
	
	public void testGetBoundaries() {
		Coordinate[] actual, expected;
		
		expected = new Coordinate[] {
			new Coordinate(0, 1, 0),
			new Coordinate(0, 2, 0),
			new Coordinate(0, 3, 0),
			new Coordinate(1, 4, 0),
			new Coordinate(2, 5, 0),
			new Coordinate(3, 5, 0),
			new Coordinate(4, 5, 0),
			new Coordinate(4, 4, 0),
			new Coordinate(4, 3, 0),
			new Coordinate(3, 2, 0),
			new Coordinate(2, 1, 0),
			new Coordinate(1, 1, 0)
		};
		
		actual = hex.getBoundaries();
		assertArraysEqual(actual, expected, true);
	}
	
	public void testCanonicalSites() {
		Coordinate[] actual, expected;

		expected = new Coordinate[] {
				new Coordinate(2, 3, 0),
				new Coordinate(1, 2, 0),
				new Coordinate(1, 3, 0),
				new Coordinate(2, 4, 0),
				new Coordinate(3, 4, 0),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(0, 1, 0),
				new Coordinate(0, 2, 0),
				new Coordinate(0, 3, 0),
				new Coordinate(1, 4, 0),
				new Coordinate(2, 5, 0),
				new Coordinate(3, 5, 0),
				new Coordinate(4, 5, 0),
				new Coordinate(4, 4, 0),
				new Coordinate(4, 3, 0),
				new Coordinate(3, 2, 0),
				new Coordinate(2, 1, 0),
				new Coordinate(1, 1, 0)
			};
			actual = hex.getCanonicalSites();
			assertArraysEqual(actual, expected, true);
	}
	
	public void testOverbounds() {
		
		// In-bounds coordinates -- origin is (2, 3)
		Coordinate a, b, c, d;
		a = new Coordinate(2, 3, 0);
		b = new Coordinate(3, 4, 0);
		c = new Coordinate(2, 1, 0);
		d = new Coordinate(4, 3, 0);
		
		Coordinate p, q, r, s, t;
		
		p = new Coordinate(5, 3, 0);   // +3u
		q = new Coordinate(5, 6, 0);   // +3v
		r = new Coordinate(2, 6, 0);   // +3w
		s = new Coordinate(-1, 5, 0);  // -3u +2w
		t = new Coordinate(-1, -1, 0); // -3v -w
		
		Coordinate actual, expected;
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(a);
		assertEquals(expected, actual);

		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(b);
		assertEquals(expected, actual);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(c);
		assertEquals(expected, actual);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(d);
		assertEquals(expected, actual);
		
		expected = new Coordinate(1, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(p);
		assertEquals(expected, actual);

		expected = new Coordinate(0, 1, 0, Flags.VECTOR);
		actual = hex.getOverbounds(q);
		assertEquals(expected, actual);
		
		expected = new Coordinate(0, 0, 1, Flags.VECTOR);
		actual = hex.getOverbounds(r);
		assertEquals(expected, actual);
		
		// Should wrap from northwest corner to southeast, and then
		// it can go up without wrapping
		expected = new Coordinate(-1, 0, 0, Flags.VECTOR);
		actual = hex.getOverbounds(s);
		assertEquals(expected, actual);
		
		// Should wrap from southwest corner to northeast, and then
		// it can go down without wrapping
		expected = new Coordinate(0, -1, 0, Flags.VECTOR);
		actual = hex.getOverbounds(t);
		assertEquals(expected, actual);
	}
	
	public void testDimensions() {
		int[] actual, expected;
		expected = new int[] {5, 5, 5};
		actual = hex.getDimensions();
		assertArraysEqual(actual, expected, false);
	}
	
	/*public void testGetLimits() {
		Coordinate[] actual, expected;

		// Even
		expected = new Coordinate[] {
			new Coordinate(-2, -2, -2, Flags.VECTOR),
			new Coordinate(2, 2, 2, Flags.VECTOR)
		};
		
		actual = hex.getLimits();
		assertArraysEqual(actual, expected, false);
	}*/
}
