/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.set;

import control.GeneralParameters;
import geometry.Geometry;
import geometry.set.CompleteSet;
import geometry.set.CoordinateSet;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by dbborens on 7/28/14.
 */
public abstract class CoordinateSetFactory {
    public static CoordinateSet instantiate(Element e, Geometry geom, GeneralParameters p) {
        // Null = complete set (no restriction)
        if (e == null) {
            return new CompleteSet(geom);
        }

        Element descriptor = getDescriptor(e);
        String name = descriptor.getName();

        if (name.equalsIgnoreCase("all")) {
            return new CompleteSet(geom);
        } else if (name.equals("disc")) {
            return DiscSetFactory.instantiate(descriptor, geom, p);
        } else if (name.equals("list")) {
            return CustomSetFactory.instantiate(descriptor, geom);
        } else if (name.equals("line")) {
            throw new UnsupportedOperationException("Coordinate set 'line' has been disabled until it is re-implemented in standard Factory logic and tested thoroughly.");
        } else if (name.equals("rectangle")) {
            throw new UnsupportedOperationException("Coordinate set 'rectangle' has been disabled until it is re-implemented in standard Factory logic and tested thoroughly.");
        } else {
            throw new IllegalArgumentException("Unrecognized coordinate set '" + name + "'");
        }
    }

    private static Element getDescriptor(Element e) {
        List<Object> children = e.elements();
        if (children.size() != 1) {
            throw new IllegalStateException("Expect single child (descriptor) for coordinate set definition, but got " + children.size() + ".");
        }

        return (Element) children.iterator().next();
    }
}
