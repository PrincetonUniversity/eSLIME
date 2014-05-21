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

package io.factory;

import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.glyph.MockGlyph;
import io.visual.highlight.HighlightManager;
import io.visual.map.MapVisualization;
import org.dom4j.Element;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class KymographFactoryTest extends EslimeLatticeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpGeometry();
        root = readXmlFile("factories/MapVisualizationFactoryTest.xml");
    }

    private void setUpGeometry() {
//        geom.setDimensionality(2);
//        geom.setConnectivity(3);
    }

    public void testTypicalCase() throws Exception {
        fail("Not yet implemented");
//        Element typicalCase = root.element("typical-case");
//        MapVisualization actual = MapVisualizationFactory.instantiate(typicalCase);
//        actual.init(geom, null, null);
//
//        MapVisualization expected = makeTypicalCase();
//        assertEquals(expected, actual);
    }

    public void testMinimalCase() throws Exception {
        fail("Not yet implemented");
//        Element minimalCase = root.element("minimal-case");
//        MapVisualization actual = MapVisualizationFactory.instantiate(minimalCase);
//        actual.init(geom, null, null);
//
//        MapVisualization expected = makeMinimalCase();
//        assertEquals(expected, actual);
    }

//    private MapVisualization makeMinimalCase() {
//        ColorManager colorManager = new DefaultColorManager();
//        HighlightManager highlightManager = new HighlightManager();
//        double edge = 10.0;
//
//        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge);
//        mapState.setHighlightManager(highlightManager);
//
//        MapVisualization mapVisualization = new MapVisualization(mapState);
//        mapVisualization.init(geom, null, null);
//        return mapVisualization;
//    }

//    private MapVisualization makeTypicalCase() {
//        ColorManager colorManager = new DefaultColorManager();
//        HighlightManager highlightManager = new HighlightManager();
//        highlightManager.setGlyph(0, new MockGlyph());
//        double edge = 5.0;
//
//        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge);
//        mapState.setHighlightManager(highlightManager);
//
//        MapVisualization mapVisualization = new MapVisualization(mapState);
//        mapVisualization.init(geom, null, null);
//        return mapVisualization;
//    }

}
