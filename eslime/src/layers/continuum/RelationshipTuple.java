/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;

/**
 * Created by dbborens on 12/30/14.
 */
public class RelationshipTuple {

    private final Coordinate coordinate;
    private final double magnitude;

    public RelationshipTuple(Coordinate coordinate, double magnitude) {
        this.coordinate = coordinate;
        this.magnitude = magnitude;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipTuple that = (RelationshipTuple) o;

        if (Double.compare(that.magnitude, magnitude) != 0) return false;
        if (!coordinate.equals(that.coordinate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = coordinate.hashCode();
        temp = Double.doubleToLongBits(magnitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
