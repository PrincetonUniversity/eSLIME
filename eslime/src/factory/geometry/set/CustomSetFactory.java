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

import control.identifiers.Coordinate;
import factory.control.identifiers.CoordinateFactory;
import geometry.Geometry;
import geometry.set.CustomSet;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/28/14.
 */
public abstract class CustomSetFactory {

    public static CustomSet instantiate(Element e, Geometry geom) {
        CustomSet ret = new CustomSet();
        for (Object o : e.elements()) {
            processElement(ret, geom, (Element) o);
        }

        return ret;
    }

    private static void processElement(CustomSet ret, Geometry geom, Element e) {
        if (e.getName().equalsIgnoreCase("coordinate")) {
            Coordinate c =  CoordinateFactory.instantiate(e);
            ret.add(c);
        } else if (e.getName().equalsIgnoreCase("offset")) {
            Coordinate c = CoordinateFactory.offset(e, geom);
            ret.add(c);
        } else {
            throw new IllegalArgumentException("Unrecognized argument '" + e.getName() + "' in <custom-set>");
        }
    }
}
