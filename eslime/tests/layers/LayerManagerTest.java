/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import factory.geometry.GeometryFactory;
import factory.layers.LayerManagerFactory;
import layers.solute.SoluteLayer;
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 12/30/13.
 */
public class LayerManagerTest extends EslimeTestCase {

    private LayerManager case1;
    private LayerManager case2;

    @Override
    protected void setUp() throws Exception {
        // Load mock layer object from a fixture file
        Element root = readXmlFile("LayerManagerFixture.xml");

        // Construct geometry manager
        Element geomElem = root.element("geometry");
        GeometryFactory gm = new GeometryFactory(geomElem);

        // Construct layer manager instances
        Element lr1 = root.element("first-case");
        case1 = LayerManagerFactory.instantiate(lr1, gm);

        Element lr2 = root.element("second-case");
        case2 = LayerManagerFactory.instantiate(lr2, gm);
    }

    public void testHasCellLayer() throws Exception {
        assertTrue(case1.hasCellLayer());
        assertFalse(case2.hasCellLayer());
    }

    public void testGetSoluteLayerIds() throws Exception {
        String[] expected = new String[]{"prr", "prh"};
        String[] actual = case1.getSoluteLayerIds();
        assertArraysEqual(expected, actual, true);
    }

    public void testGetSoluteLayer() throws Exception {
        SoluteLayer prr = case1.getSoluteLayer("prr");
        assertEquals("prr", prr.getId());
    }

    public void testGetCellLayer() throws Exception {
        assertNotNull(case1.getCellLayer());
        assertNull(case2.getCellLayer());
    }
}
