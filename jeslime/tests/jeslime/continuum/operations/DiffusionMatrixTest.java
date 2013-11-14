package jeslime.continuum.operations;

import no.uib.cipr.matrix.sparse.CompDiagMatrix;
import continuum.operations.Diffusion;
import structural.identifiers.Coordinate;
import jeslime.mock.MockGeometry;
import junit.framework.TestCase;

public class DiffusionMatrixTest extends AbstractMatrixTest {

	/*
	public void test1DLinearLattice() {
		MockGeometry geom = new LinearMockGeometry();
		
		Coordinate[] sites = new Coordinate[4];
		for (int i = 0; i < 4; i++) {
			sites[i] = new Coordinate(i, 0, 0);
		}
		
		geom.setCanonicalSites(sites);
		CompDiagMatrix mat = new Diffusion(geom, 0.1);
		
		double[] row0 = new double[] {0.8, 0.1, 0.0, 0.0};
		double[] row1 = new double[] {0.1, 0.8, 0.1, 0.0};
		double[] row2 = new double[] {0.0, 0.1, 0.8, 0.1};
		double[] row3 = new double[] {0.0, 0.0, 0.1, 0.8};

		double[][] rows = new double[][] {row0, row1, row2, row3};
		for (int i = 0; i < 4; i++) {
			double[] row = rows[i];
			
			for (int j = 0; j < 4; j++) {
				assertEquals(mat.get(i, j), row[j], 1e-10);
			}
		}
	}*/
	/*public void test2DSquareLattice() {
		MockGeometry geom = new SquareMockGeometry();
		Coordinate[] sites = new Coordinate[16];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				int i = (y * 4) + x;
					sites[i] = new Coordinate(x, y, 0);
			}
		}
	
		geom.setCanonicalSites(sites);
		CompDiagMatrix mat = new Diffusion(geom, 0.1);
	*/	
		/*
		 * 0:  0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
		 * 1:  0.1 0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
		 * ...
		 * 5:  0.0 0.1 0.0 0.0 0.1 0.6 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0
		 * ...
		 * 15: 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.1 0.6
		 */
		
		/*int[] is = new int[] {0, 1, 5, 15};
		
		double[] row0 = new double[] {0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row1 = new double[] {0.1, 0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row5 = new double[] {0.0, 0.1, 0.0, 0.0, 0.1, 0.6, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double[] row15 = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.0, 0.1, 0.6};

		double[][] rows = new double[][] {row0, row1, row5, row15};
		
		for (int k = 0; k < is.length; k++) {
			int i = is[k];
			double[] row = rows[k];
			for (int j = 0; j < 16; j++) {
				assertEquals(row[j], mat.get(i, j), 1e-10);
			}
		}
	}*/
	
	/*public void test2DTriangularLattice() {
		MockGeometry geom = new TriangularMockGeometry();
		makeTriangularLattice(geom);
		CompDiagMatrix mat = new Diffusion(geom, 0.1);
		
		// Check diagonal entry for coordinate (1, 2), indexed as (2*4) + 2 = 9
		assertEquals(0.6, mat.get(i(1, 2), i(1, 2)), 1e-10);
		
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
	
	private int i(int x, int y) {
		return (y * 4) + x;
	}
	
	private int i(int x, int y, int z) {
		return (z * 16) + (y * 4) + x;
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
	
	public void test3DCubicLattice() {
		MockGeometry geom = new CubicMockGeometry();
		makeCubicLattice(geom);
		CompDiagMatrix mat = new Diffusion(geom, 0.1);
		assertEquals(0.4, mat.get(i(2, 2, 2), i(2, 2, 2)), 1e-10);

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
	*/
	
	public void testNothing() {
		fail();
	}
}
