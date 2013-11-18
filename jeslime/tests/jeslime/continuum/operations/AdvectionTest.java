package jeslime.continuum.operations;

import continuum.operations.Advection;
import continuum.operations.ContinuumOperation;
import continuum.operations.Diffusion;
import structural.Flags;
import structural.MatrixUtils;
import structural.identifiers.Coordinate;
import jeslime.EslimeTestCase;
import jeslime.mock.CubicMockGeometry;
import jeslime.mock.LinearMockGeometry;
import jeslime.mock.MockGeometry;
import jeslime.mock.SquareMockGeometry;
import jeslime.mock.TriangularMockGeometry;

public class AdvectionTest extends EslimeTestCase {

	public void testLinear() {
		int w = 10;
		LinearMockGeometry geom = new LinearMockGeometry();
		geom.setWidth(w);
		
		Coordinate displacement = new Coordinate(1, 0, Flags.VECTOR);
		
		int size = w;
		
		Coordinate[] sites = new Coordinate[size];
		
		for (int i = 0; i < size; i++) {
			sites[i] = new Coordinate(i, 0, 0);
		}
		
		doTest(geom, displacement, sites, w);
	}
	
	public void testRectangular() {
		int width = 4;
		SquareMockGeometry geom = new SquareMockGeometry();
		geom.setWidth(width);
		Coordinate displacement = new Coordinate(1, 0, Flags.VECTOR);

		Coordinate[] sites = new Coordinate[16];
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				int i = (y * width) + x;
					sites[i] = new Coordinate(x, y, 0);
			}
		}
		
		doTest(geom, displacement, sites, width);
	}
	
	public void testTriangular() {
		int width = 4;
		TriangularMockGeometry geom = new TriangularMockGeometry();
		geom.setRadius(width);
		Coordinate displacement = new Coordinate(1, 0, 0, Flags.VECTOR);
		Coordinate[] sites = makeTriangularLattice(geom, width);
		doTest(geom, displacement, sites, width);

	}
	
	public void testCubic() {
		int w = 4;
		CubicMockGeometry geom = new CubicMockGeometry();
		geom.setWidth(w);
		Coordinate displacement = new Coordinate(1, 0, 0, Flags.VECTOR);
		Coordinate[] sites = makeCubicLattice(geom, w);
		doTest(geom, displacement, sites, w);
	}
	
	private void doTest(MockGeometry geom, Coordinate displacement, Coordinate[] sites, int width) {
		int size = sites.length;
		geom.setCanonicalSites(sites);
		ContinuumOperation mat = new Advection(geom, false, displacement, 0.25);
		mat.init();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i % width != 0 && i == j + 1) {
					assertEquals(0.25, mat.get(i, j), epsilon);
				} else if (i == j) {
					assertEquals(0.75, mat.get(i, j), epsilon);
				} else {
					assertEquals(0.0, mat.get(i, j), epsilon);
				}
			}
		}
	}
	
	private Coordinate[] makeCubicLattice(MockGeometry geom, int width) {
		Coordinate[] sites = new Coordinate[64];
		
		int i = 0;
		for (int z = 0; z < width; z++) {
			for (int y = 0; y < width; y++) {
				for (int x = 0; x < width; x++) {
					Coordinate idx = new Coordinate(x, y, z, 0);
					sites[i] = idx;
					i++;
				}
			}
		}
		geom.setCanonicalSites(sites);
		
		return sites;
	}
	
	private Coordinate[] makeTriangularLattice(MockGeometry geom, int width) {
		Coordinate[] sites = new Coordinate[16];

		int i = 0;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				
				Coordinate idx = new Coordinate(x, y, 0);
				sites[i] = idx;
				
				i++;
			}
		}
		
		geom.setCanonicalSites(sites);
		
		return sites;
	}
}
