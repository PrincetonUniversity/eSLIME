/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Created by dbborens on 4/24/14.
 */
public class Record extends CellProcess {
    public Record(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments, cpArguments);
    }

    @Override
    public void init() {
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
