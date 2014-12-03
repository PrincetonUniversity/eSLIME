/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package continuum.operations;

import control.identifiers.Coordinate;
import geometry.Geometry;

public class Advection extends Operator {

    private Coordinate displacement;
    private double r;

    public Advection(Geometry geometry, boolean useBoundaries, Coordinate displacement, double r) {
        super(geometry, useBoundaries);
        this.displacement = displacement;
        this.r = r;
    }

    @Override
    public void init() {
        for (Coordinate origin : sites) {
            // Set origin to whatever isn't removed.
            int j = geometry.coordToIndex(origin);
            augment(j, j, 1.0 - r);

            // Now weight the target by r.
            Coordinate target = geometry.rel2abs(origin, displacement, mode());

            // Handle absorbing boundaries.
            if (target != null) {
                //System.out.println("   Trying to look up " + target);
                int i = geometry.coordToIndex(target);
                augment(i, j, r);
            }
        }
    }


    public double getR() {
        return r;
    }

    public Coordinate getDisplacement() {
        return displacement;
    }
}
