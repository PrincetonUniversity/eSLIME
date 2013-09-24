package processes.temporal;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;

/**
 * Simple Gillespie process for time scaling. Assumes all cells
 * are growing at the same rate: 1 cell per arbitrary unit. So
 * choose an exponentially distributed random number whose lambda
 * is inversely proportional to the number of cells.
 * 
 * @author dbborens
 *
 */
public class SimpleGillespie extends TimeProcess {

	public SimpleGillespie(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
	}

	@Override
	public void iterate(StepState state) throws HaltCondition {
		double lambda = 1.0D / lattice.getOccupiedSites().size();
		double dt = expRandom(lambda);
		state.advanceClock(dt);
		lattice.advanceClock(dt);
	}

}
