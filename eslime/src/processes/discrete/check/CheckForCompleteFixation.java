/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.GeneralParameters;
import control.halt.FixationEvent;
import control.halt.HaltCondition;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.StateMapViewer;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForCompleteFixation extends CellProcess {
    public CheckForCompleteFixation(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);
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
        StateMapViewer smv = layer.getViewer().getStateMapViewer();

        for (Integer s : smv.getStates()) {
            if (smv.getCount(s) == layer.getGeometry().getCanonicalSites().length) {
                throw new FixationEvent(s);
            }
        }
    }
}
