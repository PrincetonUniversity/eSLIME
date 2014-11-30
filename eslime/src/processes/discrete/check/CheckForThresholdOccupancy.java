/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.arguments.Argument;
import control.halt.HaltCondition;
import control.halt.ThresholdOccupancyReachedEvent;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.discrete.CellProcessArguments;
import processes.gillespie.GillespieState;

/**
 * Throws a halt event when the system's total occupancy exceeds a specified
 * threshold.
 *
 */
public class CheckForThresholdOccupancy extends CellProcess {
    private int thresholdCount;
    private Argument<Double> thresholdOccupancy;

    public CheckForThresholdOccupancy(BaseProcessArguments arguments, CellProcessArguments cpArguments, Argument<Double> thresholdOccupancy) {
        super(arguments, cpArguments);
        this.thresholdOccupancy = thresholdOccupancy;
    }

    @Override
    public void init() {
        double toVal;

        try {
            toVal = thresholdOccupancy.next();
        } catch (HaltCondition ex) {
            throw new IllegalStateException(ex);
        }

        if (toVal > 1.0 || toVal < 0) {
            throw new IllegalArgumentException("Illegal occupancy fraction " + toVal);
        }

        thresholdCount = (int) Math.floor(layer.getGeometry().getCanonicalSites().length * toVal);
    }
    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen in this process.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        int numOccupied = layer.getViewer().getOccupiedSites().size();

        if (numOccupied >= thresholdCount) {
            throw new ThresholdOccupancyReachedEvent();
        }
    }
}
