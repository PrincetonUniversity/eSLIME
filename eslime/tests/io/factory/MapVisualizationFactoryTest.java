/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import factory.io.visual.map.MapVisualizationFactory;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.glyph.MockGlyph;
import io.visual.highlight.HighlightManager;
import io.visual.map.MapVisualization;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class MapVisualizationFactoryTest extends EslimeLatticeTestCase {
    private Element root;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpGeometry();
        root = readXmlFile("factories/MapVisualizationFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    private void setUpGeometry() {
        geom.setDimensionality(2);
        geom.setConnectivity(3);
    }

    public void testTypicalCase() throws Exception {
        Element typicalCase = root.element("typical-case");
        MapVisualization actual = MapVisualizationFactory.instantiate(typicalCase, p);
        actual.init(geom, null, null);

        MapVisualization expected = makeTypicalCase();
        assertEquals(expected, actual);
    }

    public void testMinimalCase() throws Exception {
        Element minimalCase = root.element("minimal-case");
        MapVisualization actual = MapVisualizationFactory.instantiate(minimalCase, p);
        actual.init(geom, null, null);

        MapVisualization expected = makeMinimalCase();
        assertEquals(expected, actual);
    }

    private MapVisualization makeMinimalCase() {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        int edge = 10;

        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge, 1);
        mapState.setHighlightManager(highlightManager);

        MapVisualization mapVisualization = new MapVisualization(mapState);
        mapVisualization.init(geom, null, null);
        return mapVisualization;
    }

    private MapVisualization makeTypicalCase() {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        highlightManager.setGlyph(0, new MockGlyph());
        int edge = 5;

        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge, 1);
        mapState.setHighlightManager(highlightManager);

        MapVisualization mapVisualization = new MapVisualization(mapState);
        mapVisualization.init(geom, null, null);
        return mapVisualization;
    }

}
