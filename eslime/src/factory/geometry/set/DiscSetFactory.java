/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
