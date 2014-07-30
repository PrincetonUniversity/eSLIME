/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;

/**
 * Created by David B Borenstein on 1/7/14.
 */
@SuppressWarnings("unused")
public class SourceProcess extends ContinuumProcess {

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new UnsupportedOperationException("This class won't work until after you implement source/sink active management");
    }

    public SourceProcess(ProcessLoader loader, LayerManager layerManager, GeneralParameters p, int id) {
        super(loader, layerManager, p, id);
    }
}
