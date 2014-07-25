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

package factory.io.visual;

import control.GeneralParameters;
import factory.io.visual.kymograph.KymographFactory;
import factory.io.visual.map.MapVisualizationFactory;
import io.visual.MockVisualization;
import io.visual.Visualization;
import org.dom4j.Element;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class VisualizationFactory {

    public static Visualization instantiate(Element root, GeneralParameters p) {
        String className = getClassName(root);

        if (className.equalsIgnoreCase("map")) {
            return MapVisualizationFactory.instantiate(root, p);
        } else if (className.equalsIgnoreCase("kymograph")) {
                return KymographFactory.instantiate(root, p);
        } else if (className.equalsIgnoreCase("mock")) {
            return new MockVisualization();
        } else {
            throw new IllegalArgumentException("Unrecognized visualization " +
                    "class '" + className + "'");
        }
    }

    public static String getClassName(Element root) {
        Element cnNode = root.element("class");
        if (cnNode == null) {
            throw new IllegalArgumentException("Missing required argument 'visualization' in 'visualization-serializer' tag");
        }
        String className = cnNode.getTextTrim();
        return className;
    }
}
