/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.halt.HaltCondition;
import processes.BaseProcessArguments;
import processes.EcoProcess;
import processes.gillespie.GillespieState;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public abstract class ContinuumProcess extends EcoProcess {

    public ContinuumProcess(BaseProcessArguments arguments) {
        super(arguments);
    }

    protected String getProcessClass() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }
}
