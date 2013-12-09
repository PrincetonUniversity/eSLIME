package jeslime.io.project;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import continuum.operations.Operator;
import continuum.operations.Scaling;
import structural.identifiers.Coordinate;
import jeslime.EslimeTestCase;
import jeslime.mock.MockGeometry;

public class OperatorLoaderTest extends EslimeTestCase {

	public void testGetOperators() {
		// Initialize geometry
		MockGeometry geom = new MockGeometry();
		Coordinate[] sites = new Coordinate[] {
				new Coordinate(0, 0, 0)	
		};
		geom.setCanonicalSites(sites);

		// Initialize loader
		OperatorLoader loader = new OperatorLoader(geom, true);

		// Initialize elements
		Element root = new BaseElement("children");
		for (int i = 0; i < 10; i++) {
			Element child = makeChild(i);
			root.add(child);
 		}

		Operator[] children = loader.getOperators(root);
		
		for (int i = 0; i < 10; i++) {
			checkChild(children, i);
		}
	}

	private void checkChild(Operator[] children, int i) {
		Operator child = children[i];
		double lambda = i * 1.0;
		
		assertEquals(Scaling.class, child.getClass());

		Scaling s = (Scaling) child;
		
		assertEquals(lambda, s.getLambda(), epsilon);
	}

	private Element makeChild(int i) {
		Element child = new BaseElement("scaling");

		double lambda = i * 1.0;
		String lambdaStr = Double.toString(lambda);
		addElement(child, "lambda", lambdaStr);
		return child;
	}
}
