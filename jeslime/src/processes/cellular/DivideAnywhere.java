package processes.cellular;

import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import processes.StepState;
import processes.gillespie.GillespieState;
import cells.Cell;
import structural.Flags;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;
import geometries.Geometry;

public class DivideAnywhere extends BulkDivisionProcess {

	private Coordinate[] candidates = null;
	
	public DivideAnywhere(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
	}

	
	
	public void target(GillespieState gs) throws HaltCondition {
		HashSet<Coordinate> candSet = lattice.getDivisibleSites();
		candidates = candSet.toArray(new Coordinate[0]);
		
		gs.add(getID(), candidates.length, candidates.length * 1.0D);
	}
	
	public void fire(StepState state) throws HaltCondition {
		execute(state, candidates);
		candidates = null;
	}

}
