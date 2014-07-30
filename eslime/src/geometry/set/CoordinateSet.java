/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.set;

import control.identifiers.Coordinate;

import java.util.HashSet;

/**
 * Created by dbborens on 7/28/14.
 */
public abstract class CoordinateSet extends HashSet<Coordinate> {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CoordinateSet)) {
            return false;
        }

        CoordinateSet that = (CoordinateSet) o;

        if (that.size() != this.size()) {
            return false;
        }

        for (Coordinate c : this) {
            if (!that.contains(c)) {
                return false;
            }
        }

        return true;
    }
}
