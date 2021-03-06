/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import control.identifiers.Coordinate;
import factory.io.visual.VisualizationFactory;
import factory.io.visual.kymograph.KymographFactory;
import factory.io.visual.map.MapVisualizationFactory;
import geometry.Geometry;
import geometry.MockGeometry;
import io.visual.Visualization;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.highlight.HighlightManager;
import io.visual.kymograph.Kymograph;
import io.visual.map.MapVisualization;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class VisualizationFactoryTest extends EslimeTestCase {
    private double[] times;
    private int[] frames;
    private Element fixtureRoot;
    private Geometry geom;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        fixtureRoot = readXmlFile("factories/VisualizationFactoryTest.xml");
        times = new double[] {0.0};
        frames = new int[] {0};
        geom = makeMockGeometry();
        p = makeMockGeneralParameters();
    }

    public void testMapCase() {
        Element root = fixtureRoot.element("map-case");
        Visualization actual = VisualizationFactory.instantiate(root, p);
        int edge = MapVisualizationFactory.DEFAULT_EDGE;
        int outline = MapVisualizationFactory.DEFAULT_OUTLINE;
        VisualizationProperties properties = makeProperties(edge, outline);
        Visualization expected = new MapVisualization(properties);

        actual.init(geom, times, frames);
        expected.init(geom, times, frames);

        assertEquals(expected, actual);
    }

    public void testKymographCase() {
        Element root = fixtureRoot.element("kymograph-case");
        Visualization actual = VisualizationFactory.instantiate(root, p);
        int edge = KymographFactory.DEFAULT_EDGE;
        int outline = KymographFactory.DEFAULT_OUTLINE;
        VisualizationProperties properties = makeProperties(edge, outline);
        Visualization expected = new Kymograph(properties);

        actual.init(geom, times, frames);
        expected.init(geom, times, frames);

        assertEquals(expected, actual);
    }

    private VisualizationProperties makeProperties(int edge, int outline) {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        VisualizationProperties visualizationProperties = new VisualizationProperties(colorManager, edge, outline);
        visualizationProperties.setHighlightManager(highlightManager);
        return visualizationProperties;
    }

    private Geometry makeMockGeometry() {
        Coordinate[] cc = new Coordinate[] {
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0)
        };

        MockGeometry ret = new MockGeometry();
        ret.setCanonicalSites(cc);
        ret.setDimensionality(1);
        ret.setConnectivity(1);
        return ret;
    }
}
