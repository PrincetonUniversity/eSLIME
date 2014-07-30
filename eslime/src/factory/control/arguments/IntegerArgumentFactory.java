/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.arguments.UniformInteger;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public abstract class IntegerArgumentFactory {

    public static Argument<Integer> instantiate(Element e, String name, int defaultValue, Random random) {
        // No element --> return default
        Element valueElement = e.element(name);
        if (valueElement == null) {
            return new ConstantInteger(defaultValue);
        }

        return instantiate(e, name, random);
    }

    public static Argument<Integer> instantiate(Element e, String name, Random random) {

        // No element --> return default
        Element valueElement = e.element(name);
        if (valueElement == null) {
            throw new IllegalArgumentException("No value specified for " +
                    "expected field '" + name + "' in element " +
                    e.getQualifiedName());
        }

        // No children --> return text
        Iterator children = valueElement.elementIterator();
        if (!children.hasNext()) {
            return getConstant(valueElement);
        }

        // Instantiate child
        Element stochastic = (Element) children.next();
        String className = stochastic.getName();

        if (className.equalsIgnoreCase("constant")) {
            return getConstant(stochastic);
        } else if (className.equalsIgnoreCase("uniform")) {
            return getUniformInteger(stochastic, random);
        } else {
            throw new IllegalArgumentException("Unrecognized argument type '" + className + "'");
        }
    }

    private static Argument<Integer> getUniformInteger(Element valueElement, Random random) {
        int min = instantiate(valueElement, "min", random).next();
        int max = instantiate(valueElement, "max", random).next();

        Argument<Integer> ret = new UniformInteger(min, max, random);
        return ret;
    }

    private static Argument<Integer> getConstant(Element e) {
        String valueText = e.getTextTrim();
        int ret = Integer.valueOf(valueText);
        return new ConstantInteger(ret);
    }
}
