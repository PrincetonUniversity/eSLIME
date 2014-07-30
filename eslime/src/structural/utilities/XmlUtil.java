/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural.utilities;

import org.dom4j.Element;

import java.util.List;

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
        return doGetDouble(valueElement);
    }

    public static double getDouble(Element e, String name) {
        Element valueElement = e.element(name);
        if (valueElement == null) {
            String parentName = e.getQualifiedName();
            throw new IllegalArgumentException("Expected argument " + name +
                    " in element " + parentName);
        }

        return doGetDouble(valueElement);
    }

    private static double doGetDouble(Element e) {
        String valueText = e.getTextTrim();
        double ret = Double.valueOf(valueText);
        return ret;

    }

    public static int getInteger(Element e, String name, int defaultValue) {
        Element valueElement = e.element(name);

        // No element --> use default
        if (valueElement == null) {
            return defaultValue;
        }

        String valueText = valueElement.getTextTrim();
        Integer ret = Integer.valueOf(valueText);
        return ret;
    }

    public static int[] getIntegerArray(Element e, String tokenName) {
        if (e == null) {
            return new int[0];
        }

        List<Element> children = e.elements(tokenName);

        int[] ret = new int[children.size()];
        int i = 0;
        for (Element child : children) {
            String valueText = child.getTextTrim();
            int value = Integer.valueOf(valueText);
            ret[i] = value;
            i++;
        }

        return ret;
    }

    public static String getString(Element e, String elemName, String defaultString) {
        Element stringElem = e.element(elemName);
        if (stringElem == null) {
            return defaultString;
        }

        return stringElem.getTextTrim();
    }

    public static String getString(Element e, String elemName) {
        Element stringElem = e.element(elemName);
        if (stringElem == null) {
            throw new IllegalArgumentException("Expected string element '" +
                    elemName + "' for parent element " + e.getName());
        }

        return stringElem.getTextTrim();
    }
}
