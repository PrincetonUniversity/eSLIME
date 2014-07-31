/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.halt.ManualHaltEvent;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Created by dbborens on 7/31/14.
 */
public class ManualHalt extends CellProcess {
    private String message;

    public ManualHalt(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p, String message) {
        super(loader, layerManager, activeSites, id, p);
        this.message = message;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new ManualHaltEvent(message);
    }
}
