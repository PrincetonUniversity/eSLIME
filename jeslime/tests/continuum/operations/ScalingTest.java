package continuum.operations;
import geometry.*;
import test.EslimeTestCase;
import structural.identifiers.Coordinate;

public class ScalingTest extends EslimeTestCase {
	
	public void testLinear() {
		MockGeometry geom = new LinearMockGeometry();
		
		Coordinate[] sites = new Coordinate[4];
		
		for (int i = 0; i < 4; i++) {
			sites[i] = new Coordinate(i, 0, 0);
		}

		doTest(geom, sites);

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
	
		doTest(geom, sites);
	}

	
	public void testTriangular() {
		MockGeometry geom = new TriangularMockGeometry();
		Coordinate[] sites = makeTriangularLattice(geom);
		Operator mat = new Scaling(geom, false, 0.5);
		mat.init();

		doTest(geom, sites);
	}
	
	public void testCubic() {
		MockGeometry geom = new CubicMockGeometry();
		Coordinate[] sites = makeCubicLattice(geom);
		Operator mat = new Scaling(geom, false, 0.5);
		mat.init();

		doTest(geom, sites);
	}
	
	// Since scaling doesn't deal with edges, there's no need
	// for an integration test to verify that edge cases are
	// handled correctly.
	private void doTest(MockGeometry geom, Coordinate[] sites) {
		geom.setCanonicalSites(sites);
		Operator mat = new Scaling(geom, false, 0.5);
		mat.init();
	
		for (int i = 0; i < sites.length; i++) {
			for (int j = 0; j < sites.length; j++) {
				if (i == j) {
					assertEquals(0.5, mat.get(i, j), epsilon);
				} else {
					assertEquals(0.0, mat.get(i, j), epsilon);
				}
			}
		}
	}
	
	private Coordinate[] makeCubicLattice(MockGeometry geom) {
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
		
		return sites;
	}
	
	private Coordinate[] makeTriangularLattice(MockGeometry geom) {
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
		
		return sites;
	}
	
}
