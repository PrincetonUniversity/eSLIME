package processes.continuum;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.Process;
import processes.StepState;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class SourceProcess extends ContinuumProcess {

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new UnsupportedOperationException("This class won't work until after you implement source/sink active management");
    }

    public SourceProcess(ProcessLoader loader, int id, Geometry geom) {
        super(loader, id, geom);
    }
}
