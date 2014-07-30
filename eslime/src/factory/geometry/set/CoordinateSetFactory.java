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

import control.GeneralParameters;
import geometry.Geometry;
import geometry.set.CompleteSet;
import geometry.set.CoordinateSet;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/28/14.
 */
public abstract class CoordinateSetFactory {
    public static CoordinateSet instantiate(Element e, Geometry geom, GeneralParameters p) {
        // Null = complete set (no restriction)
        if (e == null) {
            return new CompleteSet(geom);
        }

        String name = e.getName();

        if (name.equalsIgnoreCase("all")) {
            return new CompleteSet(geom);
        } else if (name.equals("disc")) {
            return DiscSetFactory.instantiate(e, geom, p);
        } else if (name.equals("list")) {
            return CustomSetFactory.instantiate(e, geom);
        } else if (name.equals("line")) {
            throw new UnsupportedOperationException("Coordinate set 'line' has been disabled until it is re-implemented in standard Factory logic and tested thoroughly.");
        } else if (name.equals("rectangle")) {
            throw new UnsupportedOperationException("Coordinate set 'rectangle' has been disabled until it is re-implemented in standard Factory logic and tested thoroughly.");
        } else {
            throw new IllegalArgumentException("Unrecognized coordinate set '" + name + "'");
        }
    }
}
