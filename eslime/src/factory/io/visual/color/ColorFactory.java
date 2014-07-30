/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
