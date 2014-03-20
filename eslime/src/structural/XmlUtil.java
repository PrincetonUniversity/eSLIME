/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package structural;

import org.dom4j.Element;

/**
 * Created by dbborens on 2/20/14.
 */
public abstract class XmlUtil {
    public static boolean getBoolean(Element e, String key) {
        Element vElem = e.element(key);
        if (vElem == null) {
            return false;
        }

        String elemText = vElem.getTextTrim();

        /*
         * <my-flag />
         *
         * This should be considered "true".
         *
         */
        if (elemText.equals("")) {
            return true;
        }

        /*
         * <my-flag>true</my-flag> should also
         * be true, while <my-flag>nonsense</my-flag>
         * should not evaluate, and so on.
         */
        return Boolean.valueOf(elemText);
    }

    public static double getDouble(Element e, String name, double defaultValue) {
        Element valueElement = e.element(name);
        if (valueElement == null) {
            return defaultValue;
        }

        String valueText = valueElement.getTextTrim();
        double ret = Double.valueOf(valueText);
        return ret;
    }

    public static int getInteger(Element e, String name, int defaultValue) {
        Element valueElement = e.element(name);
        if (valueElement == null) {
            return defaultValue;
        }

        String valueText = valueElement.getTextTrim();
        Integer ret = Integer.valueOf(valueText);
        return ret;
    }
}
