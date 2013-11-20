package jeslime.continuum.operations;

import structural.identifiers.Coordinate;
import geometry.Geometry;
import continuum.operations.Operator;
import jeslime.EslimeTestCase;
import jeslime.mock.MockGeometry;
import java.util.HashMap;

public class OperatorTest extends EslimeTestCase {

	private MockGeometry geometry;
	private ContinuumOperationStub bounded, unbounded;
	
	protected void setUp() throws Exception {
		geometry = new MockGeometry();
		geometry.setCanonicalSites(new Coordinate[0]);
		
		bounded = new ContinuumOperationStub(geometry, true);
		unbounded = new ContinuumOperationStub(geometry, false);
	}

	public void testConnectivity() {
		geometry.setConnectivity(5);
		assertEquals(5, bounded.connectivity());
	}
	
	public void testDimension() {
		geometry.setDimensionality(7);
		assertEquals(7, bounded.dimension());
	}

	public void testMode() {
		assertEquals(Geometry.APPLY_BOUNDARIES, bounded.mode());
		assertEquals(Geometry.IGNORE_BOUNDARIES, unbounded.mode());
	}

	public void testNeighbors() {
		Coordinate origin = new Coordinate(0, 0, 0);
		Coordinate p, q, r, s;
		p = new Coordinate(0, 1, 0);
		q = new Coordinate(1, 0, 0);
		r = new Coordinate(0, -1, 0);
		s = new Coordinate(-1, 0, 0);
		Coordinate[] neighbors = new Coordinate[] {p, q, r, s};
		geometry.setCanonicalSites(neighbors);
		geometry.setCellNeighbors(origin, neighbors);

		
		int[] expected = new int[] {0, 1, 2, 3};
		int[] actual = bounded.neighbors(origin);
		assertArraysEqual(expected, actual, true);
	}
	
	public class ContinuumOperationStub extends Operator {

		public ContinuumOperationStub(Geometry geometry, boolean useBoundaries) {
			super(geometry, useBoundaries);
		}

		@Override
		public void init() {
		}
		
		public int connectivity() {
			return super.connectivity();
		}
		
		public int dimension() {
			return super.dimension();
		}
		
		public int mode() {
			return super.mode();
		}
		
		public int[] neighbors(Coordinate coord) {
			return super.neighbors(coord);
		}
	}
}
