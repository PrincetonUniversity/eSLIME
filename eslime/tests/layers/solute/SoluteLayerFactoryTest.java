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

package layers.solute;

import geometry.MockGeometry;
import geometry.MockGeometryManager;
import junit.framework.TestCase;
import layers.MockLayerManager;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

/**
 * Created by dbborens on 1/6/14.
 */
public class SoluteLayerFactoryTest extends TestCase {

    /*
       There should be a test case for every class of solute layer the factory
       can instantiate.
     */

    public void testEquilibriumSolverCase() {
        Element layerRoot = new BaseElement("solute-layer");

        Element layerClass = new BaseElement("class");
        layerClass.setText("equilibrium");
        layerRoot.add(layerClass);

        Element solverRoot = new BaseElement("solver");
        Element solverClass = new BaseElement("class");
        solverClass.setText("null");
        solverRoot.add(solverClass);
        layerRoot.add(solverRoot);

        Element id = new BaseElement("id");
        id.setText("testLayer");
        layerRoot.add(id);

        MockGeometryManager gm = new MockGeometryManager();
        MockGeometry geom = new MockGeometry();
        gm.setGeometry(geom);

        MockLayerManager lm = new MockLayerManager();

        SoluteLayer layer = SoluteLayerFactory.instantiate(layerRoot, gm, lm);
        assertEquals(EquilibriumSoluteLayer.class, layer.getClass());
    }

    // Need to make sure that each tag is checked as well...
}
