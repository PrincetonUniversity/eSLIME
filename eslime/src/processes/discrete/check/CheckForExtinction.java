/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.arguments.Argument;
import control.halt.ExtinctionEvent;
import control.halt.HaltCondition;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.discrete.CellProcessArguments;
import processes.gillespie.GillespieState;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForExtinction extends CellProcess {

    private double threshold;
    private Argument<Double> thresholdArg;

    public CheckForExtinction(BaseProcessArguments arguments, CellProcessArguments cpArguments, Argument<Double> thresholdArg) {
        super(arguments, cpArguments);
        this.thresholdArg = thresholdArg;
    }

    @Override
    public void init() {
        try {
            threshold = thresholdArg.next();
        } catch (HaltCondition ex) {
            throw new IllegalStateException(ex);
        }
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
        boolean thresholdIsZero = getGeneralParameters().epsilonEquals(threshold, 0.0);
        boolean noOccupiedSites = layer.getViewer().getOccupiedSites().size() == 0;
        if (thresholdIsZero && noOccupiedSites) {
            throw new ExtinctionEvent();
        }

        double totalSites = layer.getGeometry().getCanonicalSites().length * 1.0;
        double sitesOccupied = layer.getViewer().getOccupiedSites().size() * 1.0;

        double occupancy = sitesOccupied / totalSites;

        if (occupancy < threshold) {
            throw new ExtinctionEvent();
        }
    }
}
