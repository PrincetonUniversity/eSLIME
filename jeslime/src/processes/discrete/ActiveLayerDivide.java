package processes.discrete;

import io.project.ProcessLoader;

import java.util.HashSet;

import layers.cell.CellLayer;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import geometry.Geometry;

public class ActiveLayerDivide extends BulkDivisionProcess {

	int depth;
	private Coordinate[] candidates = null;
	
	public ActiveLayerDivide(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, layer, id, geom, p);
		
		depth = Integer.valueOf(get("depth"));
	}

	public void target(GillespieState gs) throws HaltCondition {

		// Choose a random active cell.
		HashSet<Coordinate> superset = layer.getViewer().getDivisibleSites();
		HashSet<Coordinate> candSet = new HashSet<Coordinate>(superset.size());
		for (Coordinate c : superset) {
			// Look for vacancies within the active layer depth
			Coordinate[] vacancies = layer.getLookupManager().getNearestVacancies(c, depth);
			
			// It's a division candidate iff it has vacant neighbors within the
			// active layer depth (i.e., is in the active layer).
			if (vacancies.length > 0) {
				candSet.add(c);
			}
		}
		candidates = candSet.toArray(new Coordinate[0]);
		
		if (gs != null) {
			gs.add(getID(), candidates.length, candidates.length * 1.0D);
		}
	}
	
	public void fire(StepState state) throws HaltCondition {

		
		execute(state, candidates);
	}

}
