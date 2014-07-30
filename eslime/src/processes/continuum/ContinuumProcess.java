/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.Process;
import processes.gillespie.GillespieState;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public abstract class ContinuumProcess extends Process {

    public ContinuumProcess(ProcessLoader loader, LayerManager layerManager, GeneralParameters p,
                            int id) {
        super(loader, layerManager, p, id);
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
