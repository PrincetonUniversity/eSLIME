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

package io.visual.glyph;

import org.dom4j.Element;
import structural.utilities.XmlUtil;

import java.awt.*;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class GlyphFactory {

    public static final double DEFAULT_SIZE = 0.1;

    public static Glyph instantiate(Element glyphRoot) {
        String className = getClassName(glyphRoot);
        if (className.equalsIgnoreCase("mock")) {
            return new MockGlyph();
        } else if (className.equalsIgnoreCase("dot")) {
            return dotGlyph(glyphRoot);
        } else if (className.equalsIgnoreCase("bullseye")) {
            return bullseyeGlyph(glyphRoot);
        } else {
            throw new IllegalArgumentException("Unrecognized glyph class '" +
                    className + "'");
        }
    }

    private static Glyph bullseyeGlyph(Element glyphRoot) {
        Color primary = getColor(glyphRoot, "primary-color", Color.RED);
        Color secondary = getColor(glyphRoot, "secondary-color", Color.WHITE);
        double size = XmlUtil.getDouble(glyphRoot, "size", DEFAULT_SIZE);

        return new BullseyeGlyph(primary, secondary, size);
    }

    private static Glyph dotGlyph(Element glyphRoot) {
        Color color = getColor(glyphRoot, "color");
        double size = XmlUtil.getDouble(glyphRoot, "size", DEFAULT_SIZE);

        return new DotGlyph(color, size);
    }

    private static Color getColor(Element glyphRoot, String colorRoot) {
        Element colorElement = glyphRoot.element(colorRoot);
        return ColorFactory.instantiate(colorElement);

    }

    private static Color getColor(Element glyphRoot, String colorRoot,
                                  Color defaultColor) {
        Element colorElement = glyphRoot.element(colorRoot);
        return ColorFactory.instantiate(colorElement, defaultColor);

    }
    private static String getClassName(Element glyphRoot) {
        Element cnElem = glyphRoot.element("class");
        String className = cnElem.getTextTrim();
        return className;
    }
}
