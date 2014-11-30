/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.temporal;

import control.arguments.Argument;
import control.halt.HaltCondition;
import processes.BaseProcessArguments;
import processes.StepState;

/**
 * Advances the clock by a constant dt.
 *
 * @author dbborens
 */
public class Tick extends TimeProcess {

    private Argument<Double> dt;

    public Tick(BaseProcessArguments arguments, Argument<Double> dt) {

        super(arguments);
        this.dt = dt;
    }

    @Override
    public void init() {
    }
    @Override
    public void fire(StepState state) throws HaltCondition {
        state.advanceClock(dt.next());
    }

}
