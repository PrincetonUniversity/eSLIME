/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.set;

import control.identifiers.Coordinate;
import geometry.Geometry;

/**
 * Created by dbborens on 7/28/14.
 */
public class CompleteSet extends CoordinateSet {

    public CompleteSet(Geometry geom) {
        for (Coordinate c : geom.getCanonicalSites()) {
            add(c);
        }
    }

}
