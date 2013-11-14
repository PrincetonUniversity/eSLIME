package jeslime.geometry.geometry;

import jeslime.EslimeTestCase;
import jeslime.mock.MockGeometry;
import jeslime.mock.MockLattice;
import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Pseudo-unit test for basic behavior of the Geometry object.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public class GeometryTest extends EslimeTestCase {

	private Geometry geom;
	private Lattice lattice;
	private Shape shape;
	private Boundary boundary;
	
	private Coordinate p, q, r;
	
	@Override
	public void setUp() {
		lattice = new RectangularLattice();
		shape = new Rectangle(lattice, 4, 4);
		boundary = new MockBoundary(lattice, shape);
		geom = new Geometry(lattice, shape, boundary);
		
		p = new Coordinate(1, 2, 0);
		q = new Coordinate(3, 2, 0);
		r = new Coordinate(4, 2, 0); 	// Wraps around
	}
	
	public void testNeighborsApply() {
		Coordinate[] actual, expected;
		
		// Within bounds
		actual = geom.getNeighbors(p, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(2, 2, 0),
				new Coordinate(1, 3, 0),
				new Coordinate(0, 2, 0),
				new Coordinate(1, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// One neighbor of bounds
		actual = geom.getNeighbors(q, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(0, 2, Flags.BOUNDARY_APPLIED),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);

		// Origin out of bounds
		actual = geom.getNeighbors(q, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(1, 2, Flags.BOUNDARY_APPLIED),
				new Coordinate(0, 3, Flags.BOUNDARY_APPLIED),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testNeighborsFlag() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getNeighbors(q, Geometry.FLAG_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(4, 2, Flags.BOUNDARY_IGNORED),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getNeighbors(q, Geometry.FLAG_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(5, 2, Flags.BOUNDARY_IGNORED),
				new Coordinate(4, 3, Flags.BOUNDARY_IGNORED),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testNeighborsExclude() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getNeighbors(q, Geometry.EXCLUDE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getNeighbors(q, Geometry.EXCLUDE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testNeighborsIgnore() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getNeighbors(q, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(4, 2, 0),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getNeighbors(q, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(5, 2, 0),
				new Coordinate(4, 3, 0),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testAnnulusApply() {
		Coordinate[] actual, expected;
		
		// Within bounds
		actual = geom.getAnnulus(p, 1, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(2, 2, 0),
				new Coordinate(1, 3, 0),
				new Coordinate(0, 2, 0),
				new Coordinate(1, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// One neighbor of bounds
		actual = geom.getAnnulus(q, 1, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(0, 2, Flags.BOUNDARY_APPLIED),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);

		// Origin out of bounds
		actual = geom.getAnnulus(q, 1, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(1, 2, Flags.BOUNDARY_APPLIED),
				new Coordinate(0, 3, Flags.BOUNDARY_APPLIED),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testAnnulusFlag() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getAnnulus(q, 1, Geometry.FLAG_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(4, 2, Flags.BOUNDARY_IGNORED),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getAnnulus(q, 1, Geometry.FLAG_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(5, 2, Flags.BOUNDARY_IGNORED),
				new Coordinate(4, 3, Flags.BOUNDARY_IGNORED),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testAnnulusExclude() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getAnnulus(q, 1, Geometry.EXCLUDE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getAnnulus(q, 1, Geometry.EXCLUDE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testAnnulusIgnore() {
		Coordinate[] actual, expected;
		
		// One neighbor of bounds
		actual = geom.getAnnulus(q, 1, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(4, 2, 0),
				new Coordinate(3, 3, 0),
				new Coordinate(2, 2, 0),
				new Coordinate(3, 1, 0)
		};
		assertArraysEqual(actual, expected, true);
		
		// Origin out of bounds
		actual = geom.getAnnulus(q, 1, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate[] {
				new Coordinate(5, 2, 0),
				new Coordinate(4, 3, 0),
				new Coordinate(3, 2, 0),
				new Coordinate(3, 1, 0)
		};
	}
	
	public void testRel2absApply() {
		Coordinate expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED);
		int[] disp = new int[] {3, 0};
		Coordinate actual = geom.rel2abs(p, disp, Geometry.APPLY_BOUNDARIES);
		assertEquals(actual, expected);
	}
	
	public void testRel2absFlag() {
		Coordinate expected = new Coordinate(4, 2, Flags.BOUNDARY_IGNORED);
		int[] disp = new int[] {3, 0};
		Coordinate actual = geom.rel2abs(p, disp, Geometry.FLAG_BOUNDARIES);
		assertEquals(actual, expected);
	}
	
	public void testRel2absExclude() {
		int[] disp = new int[] {3, 0};
		Coordinate actual = geom.rel2abs(p, disp, Geometry.EXCLUDE_BOUNDARIES);
		assertNull(actual);
	}
	
	public void testRel2absIgnore() {
		Coordinate expected = new Coordinate(4, 2, 0);
		int[] disp = new int[] {3, 0};
		Coordinate actual = geom.rel2abs(p, disp, Geometry.IGNORE_BOUNDARIES);
		assertEquals(expected, actual);
	}
	
	public void testL1DistanceApply() {
		int actual, expected;
		
		// p->p
		actual = geom.getL1Distance(p, p, Geometry.APPLY_BOUNDARIES);
		expected = 0;
		assertEquals(actual, expected);
		
		// p->q
		actual = geom.getL1Distance(p, q, Geometry.APPLY_BOUNDARIES);
		expected = 2;
		assertEquals(actual, expected);

		// p->r
		actual = geom.getL1Distance(p, r, Geometry.APPLY_BOUNDARIES);
		expected = 1;
		assertEquals(actual, expected);

		// r->p
		actual = geom.getL1Distance(r, p, Geometry.APPLY_BOUNDARIES);
		expected = 1;
		assertEquals(actual, expected);

		// r->r
		actual = geom.getL1Distance(r, r, Geometry.APPLY_BOUNDARIES);
		expected = 0;
		assertEquals(actual, expected);
	}
	
	public void testL1DistanceIgnore() {
		int actual, expected;
		
		// p->p
		actual = geom.getL1Distance(p, p, Geometry.IGNORE_BOUNDARIES);
		expected = 0;
		assertEquals(actual, expected);
		
		// p->q
		actual = geom.getL1Distance(p, q, Geometry.IGNORE_BOUNDARIES);
		expected = 2;
		assertEquals(actual, expected);

		// p->r
		actual = geom.getL1Distance(p, r, Geometry.IGNORE_BOUNDARIES);
		expected = 3;
		assertEquals(actual, expected);

		// r->p
		actual = geom.getL1Distance(r, p, Geometry.IGNORE_BOUNDARIES);
		expected = 3;
		assertEquals(actual, expected);

		// r->r
		actual = geom.getL1Distance(r, r, Geometry.IGNORE_BOUNDARIES);
		expected = 0;
		assertEquals(actual, expected);
	}
	
	public void testL1DistanceExclude() {
		boolean threw = false;
		try {
			geom.getL1Distance(p, q, Geometry.EXCLUDE_BOUNDARIES);
		} catch (Exception ex) {
			threw = true;
		}
		assertTrue(threw);		}
	
	public void testL1DistanceFlag() {
		boolean threw = false;
		try {
			geom.getL1Distance(p, q, Geometry.FLAG_BOUNDARIES);
		} catch (Exception ex) {
			threw = true;
		}
		assertTrue(threw);	
	}
	
	public void testDisplacementApply() {
		Coordinate actual, expected;
		
		// p->p
		actual = geom.getDisplacement(p, p, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(0, 0, Flags.VECTOR);
		assertEquals(actual, expected);
		
		// p->q
		actual = geom.getDisplacement(p, q, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(2, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// p->r
		actual = geom.getDisplacement(p, r, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(-1, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// r->p
		actual = geom.getDisplacement(r, p, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(1, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// r->r
		actual = geom.getDisplacement(r, r, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(0, 0, Flags.VECTOR);
		assertEquals(actual, expected);
	}
	
	public void testDisplacementFlag() {
		boolean threw = false;
		try {
			geom.getDisplacement(p, q, Geometry.FLAG_BOUNDARIES);
		} catch (Exception ex) {
			threw = true;
		}
		assertTrue(threw);
	}
	
	public void testDisplacementExclude() {
		boolean threw = false;
		try {
			geom.getDisplacement(p, q, Geometry.EXCLUDE_BOUNDARIES);
		} catch (Exception ex) {
			threw = true;
		}
		assertTrue(threw);
	}
	
	public void testDisplacementIgnore() {
		Coordinate actual, expected;
		
		// p->p
		actual = geom.getDisplacement(p, p, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate(0, 0, Flags.VECTOR);
		assertEquals(actual, expected);
		
		// p->q
		actual = geom.getDisplacement(p, q, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate(2, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// p->r
		actual = geom.getDisplacement(p, r, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate(3, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// r->p
		actual = geom.getDisplacement(r, p, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate(-3, 0, Flags.VECTOR);
		assertEquals(actual, expected);

		// r->r
		actual = geom.getDisplacement(r, r, Geometry.IGNORE_BOUNDARIES);
		expected = new Coordinate(0, 0, Flags.VECTOR);
		assertEquals(actual, expected);
	}
	
	public void testCanonicalSites() {
		MockGeometry geom = new MockGeometry(null, null, null);
		Coordinate[] canonicals = new Coordinate[] {
				new Coordinate(1, 2, 3, 4),
				new Coordinate(0, 0, 0, 0)
		};
		geom.setCanonicalSites(canonicals);
		
		Coordinate[] expected = canonicals.clone();
		Coordinate[] actual = geom.getCanonicalSites();
		
		assertArraysEqual(expected, actual, true);
	}
	
	public void testApplyExclude() {
		Coordinate actual, expected;
		
		actual = geom.apply(p, Geometry.EXCLUDE_BOUNDARIES);
		expected = p.clone();
		assertEquals(expected, actual);
		
		actual = geom.apply(q, Geometry.EXCLUDE_BOUNDARIES);
		expected = q.clone();
		assertEquals(expected, actual);

		actual = geom.apply(r, Geometry.EXCLUDE_BOUNDARIES);
		expected = null;
		assertEquals(expected, actual);	}
	
	public void testApplyApply() {
		Coordinate actual, expected;
		
		actual = geom.apply(p, Geometry.APPLY_BOUNDARIES);
		expected = p.clone();
		assertEquals(expected, actual);
		
		actual = geom.apply(q, Geometry.APPLY_BOUNDARIES);
		expected = q.clone();
		assertEquals(expected, actual);

		actual = geom.apply(r, Geometry.APPLY_BOUNDARIES);
		expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED);
		assertEquals(expected, actual);
	}
	
	public void testApplyIgnore() {
		Coordinate actual, expected;
		
		actual = geom.apply(p, Geometry.IGNORE_BOUNDARIES);
		expected = p.clone();
		assertEquals(expected, actual);
		
		actual = geom.apply(q, Geometry.IGNORE_BOUNDARIES);
		expected = q.clone();
		assertEquals(expected, actual);

		actual = geom.apply(r, Geometry.IGNORE_BOUNDARIES);
		expected = r.clone();
		assertEquals(expected, actual);
	}
	
	public void testApplyFlag() {
		Coordinate actual, expected;
		
		actual = geom.apply(p, Geometry.FLAG_BOUNDARIES);
		expected = p.clone();
		assertEquals(expected, actual);
		
		actual = geom.apply(q, Geometry.FLAG_BOUNDARIES);
		expected = q.clone();
		assertEquals(expected, actual);

		actual = geom.apply(r, Geometry.FLAG_BOUNDARIES);
		expected = r.addFlags(Flags.BOUNDARY_IGNORED);
		assertEquals(expected, actual);	
	}
	
	public void testConnectivity() {
		MockLattice lattice = new MockLattice();
		Geometry geom = new Geometry(lattice, null, null);
		lattice.setConnectivity(5);
		assertEquals(5, geom.getConnectivity());
	}
	
	public void testDimensionality() {
		MockLattice lattice = new MockLattice();
		Geometry geom = new Geometry(lattice, null, null);
		lattice.setDimensionality(5);
		assertEquals(5, geom.getDimensionality());
	}
	
	private class MockBoundary extends Boundary {

		public MockBoundary(Lattice lattice, Shape shape) {
			super(shape, lattice);
		}

		@Override
		public Coordinate apply(Coordinate c) {
			int x = c.x() % 4;
			int y = c.y() % 4;
			
			int flags = 0;
			if (x != c.x() || y != c.y()) {
				flags |= Flags.BOUNDARY_APPLIED;
			}
			return new Coordinate(x, y, flags);
		}

		@Override
		public boolean isInfinite() {
			return false;
		}

		@Override
		protected void verify(Shape shape, Lattice lattice) {}
		
	}
}
