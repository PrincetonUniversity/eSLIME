package processes.temporal;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;

/**
 * Advances the clock by a constant dt.
 * 
 * @author dbborens
 *
 */
public class Tick extends TimeProcess {

	private double dt;
	public Tick(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
		
		dt = Double.valueOf(get("dt"));
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		state.advanceClock(dt);
		lattice.advanceClock(dt);
	}

}
