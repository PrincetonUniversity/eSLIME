package processes.continuum;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.Process;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

public abstract class SteadyStateProcess extends ContinuumProcess {
	
	public SteadyStateProcess(ProcessLoader loader, int id, Geometry geom) {
		super(loader, id, geom);
	}

	public void fire(StepState state) {
		
	}
}
