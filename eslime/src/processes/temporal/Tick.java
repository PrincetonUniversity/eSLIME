/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.temporal;

import control.GeneralParameters;
import control.arguments.Argument;
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

    private Argument<Double> dt;

    public Tick(ProcessLoader loader, LayerManager layerManager, int id,
                GeneralParameters p, Argument<Double> dt) {

        super(loader, layerManager, id, p);
        this.dt = dt;
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        state.advanceClock(dt.next());
    }

}
