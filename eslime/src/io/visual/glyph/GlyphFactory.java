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

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class GlyphFactory {

    public static Glyph instantiate(Element glyphRoot) {
        String className = getClassName(glyphRoot);
        if (className.equalsIgnoreCase("mock")) {
            return new MockGlyph();
        } else {
            throw new IllegalArgumentException("Unrecognized glyph class '" +
                    className + "'");
        }
    }

    private static String getClassName(Element glyphRoot) {
        Element cnElem = glyphRoot.element("class");
        String className = cnElem.getTextTrim();
        return className;
    }
}
