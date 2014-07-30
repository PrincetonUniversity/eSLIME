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

package factory.io.visual.color;

import control.GeneralParameters;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 4/1/14.
 */
public abstract class ColorManagerFactory {

    public static ColorManager instantiate(Element element, GeneralParameters p) {
        // If no color manager is specified, use the default color manager.

        if (element == null) {
            return new DefaultColorManager();
        }

        String className = getClassName(element);
        if (className.equalsIgnoreCase("default")) {
            return new DefaultColorManager();
        } else if (className.equalsIgnoreCase("surface-growth")) {
            return SurfaceGrowthColorManagerFactory.instantiate(element, p);
        } else {
            throw new IllegalArgumentException("Unrecognized color manager class '" + className + "'");
        }
    }

    private static String getClassName(Element element) {
        Element classNameElem = element.element("class");
        return classNameElem.getTextTrim();
    }
}
