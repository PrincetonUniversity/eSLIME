/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
        if (classNameElem == null) {
            throw new IllegalArgumentException("Missing required argument <class> in item <color>");
        }
        return classNameElem.getTextTrim();
    }
}
