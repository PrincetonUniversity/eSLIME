/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.solute;

import control.identifiers.Coordinate;

/**
 * TODO this is redundant with Extrema
 */
public class Extremum implements Comparable {

    private double value;
    private Coordinate argument;
    private double time;

    public Extremum(Coordinate argument, double time, double value) {
        this.value = value;
        this.argument = argument;
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public Coordinate getArgument() {
        return argument;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Object other) {
        Extremum o = (Extremum) other;

        if (this.value > o.value) {
            return 1;
        }

        if (this.value < o.value) {
            return -1;
        }

        return 0;
    }

}
