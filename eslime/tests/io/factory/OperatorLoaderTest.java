/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.factory;

import continuum.operations.Operator;
import continuum.operations.Scaling;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import io.loader.OperatorLoader;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import test.EslimeTestCase;

public class OperatorLoaderTest extends EslimeTestCase {

    public void testGetOperators() {
        // Initialize geometry
        MockGeometry geom = new MockGeometry();
        Coordinate[] sites = new Coordinate[]{
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
