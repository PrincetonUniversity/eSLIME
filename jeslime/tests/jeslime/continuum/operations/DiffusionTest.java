package jeslime.continuum.operations;

import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingReflecting;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.Flags;
import structural.identifiers.Coordinate;
import continuum.operations.Advection;
import continuum.operations.Operator;
import continuum.operations.Diffusion;
import jeslime.EslimeTestCase;
import jeslime.mock.CubicMockGeometry;
import jeslime.mock.LinearMockGeometry;
import jeslime.mock.MockGeometry;
import jeslime.mock.SquareMockGeometry;
import jeslime.mock.TriangularMockGeometry;

public class DiffusionTest extends EslimeTestCase {
	
	public void testLinear() {
		MockGeometry geom = new LinearMockGeometry();
		
		Coordinate[] sites = new Coordinate[4];
		
		for (int i = 0; i < 4; i++) {
			sites[i] = new Coordinate(i, 0, 0);
		}
		
		geom.setCanonicalSites(sites);
		Operator mat = new Diffusion(geom, false, 0.1);
		mat.init();
		
		double[] row0 = new double[] {0.8, 0.1, 0.0, 0.0};
		double[] row1 = new double[] {0.1, 0.8, 0.1, 0.0};
		double[] row2 = new double[] {0.0, 0.1, 0.8, 0.1};
		double[] row3 = new double[] {0.0, 0.0, 0.1, 0.8};

		double[][] rows = new double[][] {row0, row1, row2, row3};
		for (int i = 0; i < 4; i++) {
			double[] row = rows[i];
			
			for (int j = 0; j < 4; j++) {
				assertEquals(mat.get(i, j), row[j], epsilon);
			}
		}
	}

	public void testRectangular() {
		MockGeometry geom = new SquareMockGeometry();
		Coordinate[] sites = new Coordinate[16];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				int i = (y * 4) + x;
					sites[i] = new Coordinate(x, y, 0);
			}
		}
	
		geom.setCanonicalSites(sites);
		Operator mat = new Diffusion(geom, false, 0.1);
		mat.init();
		
		/*
		 * 0:  0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
		 * 1:  0.1 0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
		 * ...
		 * 5:  0.0 0.1 0.0 0.0 0.1 0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0
		 * ...
		 * 15: 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.1 0.6
		 */
		
		int[] is = new int[] {0, 1, 5, 15};
		
		double[] row0 = new double[] {0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row1 = new double[] {0.1, 0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row5 = new double[] {0.0, 0.1, 0.0, 0.0, 0.1, 0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row15 = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.0, 0.1, 0.6};

		double[][] rows = new double[][] {row0, row1, row5, row15};
		
		for (int k = 0; k < is.length; k++) {
			int i = is[k];
			double[] row = rows[k];
			for (int j = 0; j < 16; j++) {
				assertEquals(row[j], mat.get(i, j), epsilon);
			}
		}
	}
	
	public void testTriangular() {
		MockGeometry geom = new TriangularMockGeometry();
		makeTriangularLattice(geom);
		Operator mat = new Diffusion(geom, false, 0.1);
		mat.init();

		// Check diagonal entry for coordinate (1, 2), indexed as (2*4) + 2 = 9
		assertEquals(0.6, mat.get(i(1, 2), i(1, 2)), epsilon);
		
		// Check neighbors of (1, 2). Note that these are not in the "true" coordinate
		// system used in HexArena and HexRing (where y is adjusted by x/2), but it 
		// doesn't matter--we specified these as the mock "neighbors," so if they agree
		// with the input, the method works as intended. (Tri lattice diffusion is tested
		// elsewhere.)
		int[] neighbors = new int[] {i(1, 3), i(2, 3), i(2, 2), i(1, 1), i(0, 1), i(0, 2)};

				
		for (int neighbor : neighbors) {
			assertEquals(0.1, mat.get(i(1, 2), neighbor));
		}
	}
	
	public void testCubic() {
		MockGeometry geom = new CubicMockGeometry();
		makeCubicLattice(geom);
		Operator mat = new Diffusion(geom, false, 0.1);
		mat.init();

		assertEquals(0.4, mat.get(i(2, 2, 2), i(2, 2, 2)), epsilon);

	}
	
	/**
	 * Integration test--make sure that the operator does
	 * the right thing when it encounters nontrivial edges.
	 */
	public void testEdgeCase() {
		// Initialize a "real" geometry with a reflecting dimension.
		Lattice lattice = new RectangularLattice();
		Shape shape = new Rectangle(lattice, 4, 4);
		Boundary boundary = new PlaneRingReflecting(shape, lattice);
		Geometry geom = new Geometry(lattice, shape, boundary);
		
		// With and without boundary conditions
		Operator with = new Diffusion(geom, true, 0.1);
		with.init();
		
		//Operator without = new Diffusion(geom, false, 0.1);
		//without.init();
		
		Coordinate o, p, q;
		int oi, pi, qi;

		// Look north from an interior coordinate
		o = new Coordinate(1, 1, 0);
		p = new Coordinate(1, 2, 0);

		oi = geom.coordToIndex(o);
		pi = geom.coordToIndex(p);
		
		assertEquals(0.6, with.get(oi, oi), epsilon);
		assertEquals(0.1, with.get(pi, oi), epsilon);
		
		//assertEquals(0.6, without.get(oi, oi), epsilon);
		//assertEquals(0.1, without.get(pi, oi), epsilon);
				
		// Look north from an exterior coordinate
		q = new Coordinate(1, 3, 0);
		qi = geom.coordToIndex(q);
		
		// With reflecting boundary conditions, diffusion back
		// to a boundary position will be negated. Without them,
		// the diffused quantity will be absorbed at the boundary.
		assertEquals(0.7, with.get(qi, qi), epsilon);
		//assertEquals(0.6, without.get(qi, qi), epsilon);
	}
	
	private void makeCubicLattice(MockGeometry geom) {
		Coordinate[] sites = new Coordinate[64];
		
		int i = 0;
		for (int z = 0; z < 4; z++) {
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					Coordinate idx = new Coordinate(x, y, z, 0);
					sites[i] = idx;
					i++;
				}
			}
		}
		geom.setCanonicalSites(sites);
	}
	
	private void makeTriangularLattice(MockGeometry geom) {
		Coordinate[] sites = new Coordinate[16];

		int i = 0;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				
				Coordinate idx = new Coordinate(x, y, 0);
				sites[i] = idx;
				
				i++;
			}
		}
		
		geom.setCanonicalSites(sites);
	}
	
	private int i(int x, int y) {
		return (y * 4) + x;
	}
	
	private int i(int x, int y, int z) {
		return (z * 16) + (y * 4) + x;
	}
}
