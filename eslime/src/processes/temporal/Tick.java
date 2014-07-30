/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.temporal;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;

/**
 * Advances the clock by a constant dt.
 *
 * @author dbborens
 */
public class Tick extends TimeProcess {

    private double dt;

    public Tick(ProcessLoader loader, LayerManager layerManager, int id,
                GeneralParameters p) {

        super(loader, layerManager, id, p);

        dt = Double.valueOf(get("dt"));
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        state.advanceClock(dt);
    }

}
