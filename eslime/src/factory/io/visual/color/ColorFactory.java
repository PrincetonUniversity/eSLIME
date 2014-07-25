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

import org.dom4j.Element;

import java.awt.*;

/**
 * Created by dbborens on 4/4/14.
 */
public abstract class ColorFactory {

    public static Color instantiate(Element root, Color defaultColor) {
        if (root == null) {
            return defaultColor;
        }

        // For the moment, we only support hex color encoding.
        Element hexElement = root.element("hex");

        if (hexElement == null) {
            throw new IllegalArgumentException("You must specify the hex code for the color you want.");
        }

        String hex = "0x" + hexElement.getTextTrim();
        Color color = Color.decode(hex);
        return color;
    }
}
