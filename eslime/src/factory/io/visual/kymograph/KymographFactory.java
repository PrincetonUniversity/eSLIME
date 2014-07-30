/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.io.visual.kymograph;

import control.GeneralParameters;
import factory.io.visual.highlight.HighlightManagerFactory;
import factory.io.visual.color.ColorManagerFactory;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.highlight.HighlightManager;
import io.visual.kymograph.Kymograph;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class KymographFactory {

    public static final double DEFAULT_EDGE = 10.0;

    // You often don't want a cell outline for a kymograph. Default to 0.
    public static final int DEFAULT_OUTLINE = 0;

    public static Kymograph instantiate(Element mapElement, GeneralParameters p) {
        // Make highlight manager
        HighlightManager highlightManager = makeHighlightManager(mapElement);

        // Make color manager
        ColorManager colorManager = makeColorManager(mapElement, p);

        // Get edge size
        double edge = getEdge(mapElement);

        // Get outline thickness
        int outline = getOutline(mapElement);

        // Construct map state object
        VisualizationProperties properties = makeProperties(highlightManager, colorManager, edge, outline);

        // Construct map
        Kymograph kymograph = new Kymograph(properties);

        return kymograph;
    }

    private static VisualizationProperties makeProperties(HighlightManager highlightManager,
                                                          ColorManager colorManager,
                                                          double edge, int outline) {

        VisualizationProperties properties = new VisualizationProperties(colorManager, edge, outline);
        properties.setHighlightManager(highlightManager);
        return properties;
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
