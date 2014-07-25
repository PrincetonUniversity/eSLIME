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

import control.GeneralParameters;
import io.factory.io.visual.color.ColorManagerFactory;
import io.visual.color.ColorManager;
import io.visual.highlight.HighlightManager;
import io.visual.VisualizationProperties;
import io.visual.map.MapVisualization;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class MapVisualizationFactory {

    public static final double DEFAULT_EDGE = 10.0;
    public static final int DEFAULT_OUTLINE = 1;

    public static MapVisualization instantiate(Element mapElement, GeneralParameters p) {
        // Make highlight manager
        HighlightManager highlightManager = makeHighlightManager(mapElement);

        // Make color manager
        ColorManager colorManager = makeColorManager(mapElement, p);

        // Get edge size
        double edge = getEdge(mapElement);

        int outline = getOutline(mapElement);

        // Construct map state object
        VisualizationProperties visualizationProperties = makeVisualizationProperties(highlightManager, colorManager, edge, outline);

        // Construct map
        MapVisualization map = new MapVisualization(visualizationProperties);

        return map;
    }

    private static VisualizationProperties makeVisualizationProperties(HighlightManager highlightManager,
                                                                       ColorManager colorManager,
                                                                       double edge, int outline) {

        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge, outline);
        mapState.setHighlightManager(highlightManager);
        return mapState;
    }

    private static HighlightManager makeHighlightManager(Element root) {
        Element highlightRoot = root.element("highlights");
        HighlightManager ret = HighlightManagerFactory.instantiate(highlightRoot);
        return ret;
    }

    private static ColorManager makeColorManager(Element root, GeneralParameters p) {
        Element colorRoot = root.element("color");
        ColorManager ret = ColorManagerFactory.instantiate(colorRoot, p);
        return ret;
    }

    private static double getEdge(Element root) {
        double edge = XmlUtil.getDouble(root, "edge", DEFAULT_EDGE);
        return edge;
    }

    private static int getOutline(Element root) {
        int outline = XmlUtil.getInteger(root, "outline", DEFAULT_OUTLINE);
        return outline;
    }
}
