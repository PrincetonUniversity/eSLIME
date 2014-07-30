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

package geometry.set;

import control.arguments.Argument;
import control.identifiers.Coordinate;
import geometry.Geometry;

/**
 * Created by dbborens on 7/28/14.
 */
public class DiscSet extends CoordinateSet {

    public DiscSet(Geometry geom, Argument<Integer> radiusArg, Coordinate offset) {
        Coordinate origin = geom.rel2abs(geom.getCenter(), offset, Geometry.APPLY_BOUNDARIES);
        int radius = radiusArg.next();

        for (int r = 0; r <= radius; r++) {
            Coordinate[] annulus = geom.getAnnulus(origin, r, Geometry.APPLY_BOUNDARIES);
            for (Coordinate c : annulus) {
                add(c);
            }
        }
    }
}
