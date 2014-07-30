/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.identifiers;


public class NonZeroExtrema extends Extrema {

    @Override
    public boolean consider(double u, Coordinate c, double t) {
        boolean assigned = false;
        if (u != 0 && u > max) {
            TemporalCoordinate tc = new TemporalCoordinate(c, t);
            max = u;
            argMax = tc;
            assigned = true;
        }

        if (u != 0 && u < min) {
            TemporalCoordinate tc = new TemporalCoordinate(c, t);
            min = u;
            argMin = tc;
            assigned = true;
        }

        return assigned;
    }
}
