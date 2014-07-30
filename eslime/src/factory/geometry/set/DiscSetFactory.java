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

package factory.geometry.set;

import control.GeneralParameters;
import control.arguments.Argument;
import control.identifiers.Coordinate;
import factory.control.arguments.IntegerArgumentFactory;
import factory.control.identifiers.CoordinateFactory;
import geometry.Geometry;
import geometry.set.DiscSet;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/28/14.
 */
public abstract class DiscSetFactory {
    public static DiscSet instantiate(Element e, Geometry geom, GeneralParameters p) {
        Argument<Integer> radiusArg = IntegerArgumentFactory.instantiate(e, "radius", 1, p.getRandom());
        Coordinate offset = getOffset(e, geom);
        return new DiscSet(geom, radiusArg, offset);
    }

    private static Coordinate getOffset(Element e, Geometry geom) {
        Element offsetElem = e.element("offset");

        if (offsetElem == null) {
            return geom.getZeroVector();
        }
        return CoordinateFactory.instantiate(offsetElem);
    }
}
