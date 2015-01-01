/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import structural.Trigger;

import java.util.function.Supplier;

/**
 * Created by dbborens on 12/31/14.
 */
public class ContinuumLinkage {

    // The value of this cell's effect upon the field
    private double magnitude;

    // Retrieves the value of the continuum at this cell's location
    private Supplier<Double> valueGetter;

    // Notifies the field that this cell should be removed
    private Trigger remover;

    public ContinuumLinkage(Trigger remover,
                            Supplier<Double> valueGetter,
                            double magnitude) {

        this.remover = remover;
        this.valueGetter = valueGetter;
        this.magnitude = magnitude;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public Double getValue() {
        return valueGetter.get();
    }

    public void remove() {
        remover.go();
    }
}
