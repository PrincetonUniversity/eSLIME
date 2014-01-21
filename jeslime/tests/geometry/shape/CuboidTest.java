package geometry.shape;

import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import structural.Flags;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

public class CuboidTest extends EslimeTestCase {
	private Shape odd;
	private Shape even;

    Lattice evenLattice;
	@Override
	public void setUp() {
		Lattice oddLattice = new CubicLattice();
		evenLattice = new CubicLattice();
		
		even = new Cuboid(evenLattice, 2, 2, 2);
		odd = new Cuboid(oddLattice, 3, 3, 3);
	}
	
	public void testGetCenter() {
		Coordinate actual, expected;
		
		// Even -- we round down
		expected = new Coordinate(0, 0, 0, 0);
		actual = even.getCenter();
		assertEquals(expected, actual);
		
		// Odd
		expected = new Coordinate(1, 1, 1, 0);
		actual = odd.getCenter();
		assertEquals(expected, actual);
	}
	
	public void testGetBoundaries() {
		Coordinate[] actual, expected;
		
		// Even -- all points are on boundary
		expected = new Coordinate[] {
			new Coordinate(0, 0, 0, 0),
			new Coordinate(0, 0, 1, 0),
			new Coordinate(0, 1, 0, 0),
			new Coordinate(0, 1, 1, 0),
			new Coordinate(1, 0, 0, 0),
			new Coordinate(1, 0, 1, 0),
			new Coordinate(1, 1, 0, 0),
			new Coordinate(1, 1, 1, 0)
		};
		
		actual = even.getBoundaries();
		assertArraysEqual(actual, expected, true);
		
		// Odd -- every point but (1, 1, 1)
		expected = new Coordinate[] {
				new Coordinate(0, 0, 0, 0),
				new Coordinate(0, 0, 1, 0),
				new Coordinate(0, 0, 2, 0),
				new Coordinate(0, 1, 0, 0),
				new Coordinate(0, 1, 1, 0),
				new Coordinate(0, 1, 2, 0),
				new Coordinate(0, 2, 0, 0),
				new Coordinate(0, 2, 1, 0),
				new Coordinate(0, 2, 2, 0),
				new Coordinate(1, 0, 0, 0),
				new Coordinate(1, 0, 1, 0),
				new Coordinate(1, 0, 2, 0),
				new Coordinate(1, 1, 0, 0),
				// Not (1, 1, 1)
				new Coordinate(1, 1, 2, 0),
				new Coordinate(1, 2, 0, 0),
				new Coordinate(1, 2, 1, 0),
				new Coordinate(1, 2, 2, 0),
				new Coordinate(2, 0, 0, 0),
				new Coordinate(2, 0, 1, 0),
				new Coordinate(2, 0, 2, 0),
				new Coordinate(2, 1, 0, 0),
				new Coordinate(2, 1, 1, 0),
				new Coordinate(2, 1, 2, 0),
				new Coordinate(2, 2, 0, 0),
				new Coordinate(2, 2, 1, 0),
				new Coordinate(2, 2, 2, 0)
		};
		actual = odd.getBoundaries();
		assertArraysEqual(actual, expected, true);
	}
	
	public void testCanonicalSites() {
		Coordinate[] actual, expected;

			expected = new Coordinate[] {
				new Coordinate(0, 0, 0, 0),
				new Coordinate(0, 0, 1, 0),
				new Coordinate(0, 1, 0, 0),
				new Coordinate(0, 1, 1, 0),
				new Coordinate(1, 0, 0, 0),
				new Coordinate(1, 0, 1, 0),
				new Coordinate(1, 1, 0, 0),
				new Coordinate(1, 1, 1, 0)
			};
			actual = even.getCanonicalSites();
			assertArraysEqual(actual, expected, true);
	}
	
	public void testOverbounds() {
		Coordinate actual, expected;

		// Test coordinates -- in bounds
		Coordinate a, b, c;	
		
		a = new Coordinate(0, 0, 0, 0);
		b = new Coordinate(0, 1, 0, 0);
		c = new Coordinate(1, 1, 1, 0);
		
		// Test coordinates -- out of bounds
		Coordinate p, q, r;
		p = new Coordinate(3, 0, 0, 0);
		q = new Coordinate(-3, 3, 5, 0);
		r = new Coordinate(2, 2, 2, 0);
		
		// Even
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = even.getOverbounds(a);
		assertEquals(actual, expected);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = even.getOverbounds(b);
		assertEquals(actual, expected);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = even.getOverbounds(c);
		assertEquals(actual, expected);
		
		expected = new Coordinate(2, 0, 0, Flags.VECTOR);
		actual = even.getOverbounds(p);
		assertEquals(actual, expected);
		
		expected = new Coordinate(-3, 2, 4, Flags.VECTOR);
		actual = even.getOverbounds(q);
		assertEquals(actual, expected);
		
		expected = new Coordinate(1, 1, 1, Flags.VECTOR);
		actual = even.getOverbounds(r);
		assertEquals(actual, expected);
		
		// Odd
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = odd.getOverbounds(a);
		assertEquals(actual, expected);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = odd.getOverbounds(b);
		assertEquals(actual, expected);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = odd.getOverbounds(c);
		assertEquals(actual, expected);
		
		expected = new Coordinate(1, 0, 0, Flags.VECTOR);
		actual = odd.getOverbounds(p);
		assertEquals(actual, expected);
		
		expected = new Coordinate(-3, 1, 3, Flags.VECTOR);
		actual = odd.getOverbounds(q);
		assertEquals(actual, expected);
		
		expected = new Coordinate(0, 0, 0, Flags.VECTOR);
		actual = odd.getOverbounds(r);
		assertEquals(actual, expected);
	}
	
	public void testDimensions() {
		int[] actual, expected;
		
		// Odd
		expected = new int[] {3, 3, 3};
		actual = odd.getDimensions();
		assertArraysEqual(actual, expected, false);
		
		// Even
		expected = new int[] {2, 2, 2};
		actual = even.getDimensions();
		assertArraysEqual(actual, expected, false);
	}

    public void testCloneAtScale() {
        Lattice clonedLattice = evenLattice.clone();
        Shape cloned = even.cloneAtScale(clonedLattice, 2.0);

        assertEquals(even.getClass(), cloned.getClass());
        assertEquals(8, even.getCanonicalSites().length);
        assertEquals(64, cloned.getCanonicalSites().length);
    }

	/*public void testGetLimits() {
		Coordinate[] actual, expected;

		// Even
		expected = new Coordinate[] {
			new Coordinate(0, 0, 0, Flags.VECTOR),
			new Coordinate(1, 1, 1, Flags.VECTOR)
		};
		
		actual = even.getLimits();
		assertArraysEqual(actual, expected, false);
		
		// Odd
		expected = new Coordinate[] {
				new Coordinate(-1, -1, -1, Flags.VECTOR),
				new Coordinate(1, 1, 1, Flags.VECTOR)
		};
		
		actual = odd.getLimits();
		assertArraysEqual(actual, expected, false);
	}*/
}
