package processes.continuum;

import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import processes.Process;
import processes.StepState;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
@SuppressWarnings("unused")
public class SourceProcess extends ContinuumProcess {

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new UnsupportedOperationException("This class won't work until after you implement source/sink active management");
    }

    public SourceProcess(ProcessLoader loader, LayerManager layerManager, int id) {
        super(loader, layerManager, id);
    }
}
