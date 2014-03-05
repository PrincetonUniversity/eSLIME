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

package continuum.operations;

import test.EslimeTestCase;
import geometry.Geometry;
import geometry.MockGeometry;
import structural.identifiers.Coordinate;

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
		assertEquals(Geometry.EXCLUDE_BOUNDARIES, unbounded.mode());
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
