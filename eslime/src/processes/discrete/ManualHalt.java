/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import control.halt.ManualHaltEvent;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Created by dbborens on 7/31/14.
 */
public class ManualHalt extends CellProcess {
    private String message;

    public ManualHalt(BaseProcessArguments arguments, CellProcessArguments cpArguments, String message) {
        super(arguments, cpArguments);
        this.message = message;
    }

    @Override
    public void init() {
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new ManualHaltEvent(message);
    }
}
