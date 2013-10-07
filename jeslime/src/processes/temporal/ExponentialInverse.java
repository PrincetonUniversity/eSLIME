package processes.temporal;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;

/**
 * Simple stochastic process for time scaling. Assumes all cells
 * are growing at the same rate: 1 cell per arbitrary unit. So
 * choose an exponentially distributed random number whose lambda
 * is inversely proportional to the number of cells. This is
 * equivalent to a Gillespie process in the case where cell division
 * is the only process and where all cells have an equal
 * probability of dividing.
 * 
 * @author dbborens
 *
 */
public class ExponentialInverse extends TimeProcess {

	public ExponentialInverse(ProcessLoader loader, Lattice lattice, int id,
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
