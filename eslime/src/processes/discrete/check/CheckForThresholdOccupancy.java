/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.halt.ThresholdOccupancyReachedEvent;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Throws a halt event when the system's total occupancy exceeds a specified
 * threshold.
 *
 */
public class CheckForThresholdOccupancy extends CellProcess {
    private int thresholdCount;
    public CheckForThresholdOccupancy(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p, Argument<Double> thresholdOccupancy) {
        super(loader, layerManager, activeSites, id, p);
        double toVal = thresholdOccupancy.next();
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
            throw new ThresholdOccupancyReachedEvent(state.getTime());
        }
    }
}
