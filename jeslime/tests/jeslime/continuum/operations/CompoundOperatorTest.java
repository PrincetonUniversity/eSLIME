package jeslime.continuum.operations;

import jeslime.mock.*;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import structural.EpsilonUtil;
import structural.identifiers.Coordinate;
import continuum.operations.CompoundOperator;
import continuum.operations.Operator;
import continuum.operations.Scaling;
import jeslime.EslimeTestCase;

/**
 * Test to make sure that the compound operator class works
 * correctly. It was easiest to implement this as a de facto
 * integration test, rather than to add full mock capabilities
 * to the compound operator test.
 * 
 * @author dbborens
 *
 */
public class CompoundOperatorTest extends EslimeTestCase {

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

    /**
     * Makes sure that negative value entries in child operators
     * still get included. Regression test from prior bug.
     */
    public void testNegativeValueRegression() {
        MockGeometry geom = new MockGeometry();
        Coordinate origin = new Coordinate(0, 0, 0);
        geom.setCanonicalSites(new Coordinate[] {origin});
        MockOperator child = new MockOperator(geom, false);
        child.set(0, 0, -1.0);

        Operator parent = new CompoundOperator(geom, false, new Operator[] {child});
        parent.init();

        assertEquals(-1.0, parent.get(0, 0), EpsilonUtil.epsilon());
    }

	// Since the CompoundOperator is just the superposition
	// of other operators, there's no need to test its edge
	// handling. This will be right iff the child operators
	// handle it right.
	
	private void doTest(MockGeometry geom, Coordinate[] sites) {
		geom.setCanonicalSites(sites);
		
		Element e1 = new BaseElement("scaling");
		Element e2 = new BaseElement("scaling");
		addElement(e1, "lambda", "0.1");
		addElement(e2, "lambda", "0.4");
		
		Element compound = new BaseElement("children");
		compound.add(e1);
		compound.add(e2);
		

		Operator mat = new CompoundOperator(geom, true, compound);
		mat.init();
		
		//System.out.println(MatrixUtils.matrixForm(mat));
		
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
