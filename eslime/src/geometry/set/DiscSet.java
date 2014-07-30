/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
