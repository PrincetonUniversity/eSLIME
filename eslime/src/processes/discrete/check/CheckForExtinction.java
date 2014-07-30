/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.ExtinctionEvent;
import control.halt.HaltCondition;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForExtinction extends CellProcess {

    private double threshold;
    public CheckForExtinction(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, Argument<Double> thresholdArg, GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);
        threshold = thresholdArg.next();
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
        // Handle true extinction exactly
        boolean thresholdIsZero = p.epsilonEquals(threshold, 0.0);
        boolean noOccupiedSites = layer.getViewer().getOccupiedSites().size() == 0;
        if (thresholdIsZero && noOccupiedSites) {
            throw new ExtinctionEvent(state.getTime());
        }

        double totalSites = layer.getGeometry().getCanonicalSites().length * 1.0;
        double sitesOccupied = layer.getViewer().getOccupiedSites().size() * 1.0;

        double occupancy = sitesOccupied / totalSites;

        if (occupancy < threshold) {
            throw new ExtinctionEvent(state.getTime());
        }
    }
}
