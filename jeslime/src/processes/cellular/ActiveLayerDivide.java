package processes.cellular;

import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import processes.StepState;


import cells.Cell;

import structural.Flags;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

import geometries.Geometry;

public class ActiveLayerDivide extends BulkDivisionProcess {

	int depth;
	public ActiveLayerDivide(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
		
		depth = Integer.valueOf(get("depth"));
	}

	public void iterate(StepState state) throws HaltCondition {

		// Choose a random active cell.
		HashSet<Coordinate> superset = lattice.getDivisibleSites();
		HashSet<Coordinate> candSet = new HashSet<Coordinate>(superset.size());
		for (Coordinate c : superset) {
			// Look for vacancies within the active layer depth
			Coordinate[] vacancies = lattice.getNearestVacancies(c, depth);
			
			// It's a division candidate iff it has vacant neighbors within the
			// active layer depth (i.e., is in the active layer).
			if (vacancies.length > 0) {
				candSet.add(c);
			}
		}
		Coordinate[] candidates = candSet.toArray(new Coordinate[0]);
		
		execute(state, candidates);
	}

}
