package processes.temporal;

import geometry.Geometry;
import jeslime.io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;

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

	public ExponentialInverse(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, layer, id, geom, p);
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		double lambda = 1.0D / layer.getViewer().getOccupiedSites().size();
		double dt = expRandom(lambda);
		state.advanceClock(dt);
	}

}
