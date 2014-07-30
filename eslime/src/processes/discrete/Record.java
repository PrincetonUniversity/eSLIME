/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import control.halt.HaltCondition;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Created by dbborens on 4/24/14.
 */
public class Record extends CellProcess {
    public Record(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p) {
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
        state.record(layerManager.getCellLayer());
    }
}
