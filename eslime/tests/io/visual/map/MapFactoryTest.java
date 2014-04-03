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

package io.visual.map;

import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.glyph.MockGlyph;
import io.visual.highlight.HighlightManager;
import org.dom4j.Element;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class MapFactoryTest extends EslimeLatticeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpGeometry();
        root = readXmlFile("MapFactoryTest.xml");
    }

    private void setUpGeometry() {
        geom.setDimensionality(2);
        geom.setConnectivity(3);
    }

    public void testInstantiate() throws Exception {
        MapVisualization actual = MapFactory.instantiate(root);
        actual.init(geom);

        MapVisualization expected = makeExpected();
        assertEquals(expected, actual);
    }

    private MapVisualization makeExpected() {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        highlightManager.setGlyph(0, new MockGlyph());
        double edge = 10.0;

        MapState mapState = new MapState(colorManager, edge);
        mapState.setHighlightManager(highlightManager);

        MapVisualization mapVisualization = new MapVisualization(mapState);
        mapVisualization.init(geom);
        return mapVisualization;
    }
}
