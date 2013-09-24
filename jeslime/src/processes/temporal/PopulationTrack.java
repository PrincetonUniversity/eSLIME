package processes.temporal;

import geometries.Geometry;
import io.parameters.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;

public class PopulationTrack extends TimeProcess {

	public PopulationTrack(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
	}

	@Override
	public void iterate(StepState state) throws HaltCondition {
		double dt = lattice.getOccupiedSites().size() * 1.0D;
		state.advanceClock(dt);
		lattice.advanceClock(dt);
	}

}
