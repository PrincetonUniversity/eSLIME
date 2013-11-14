package processes.cellular;

import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import processes.StepState;
import processes.gillespie.GillespieState;
import cells.Cell;
import structural.Flags;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;
import geometry.Geometry;

public class DivideAnywhere extends BulkDivisionProcess {

	private Coordinate[] candidates = null;
	
	public DivideAnywhere(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, layer, id, geom, p);
	}

	
	
	public void target(GillespieState gs) throws HaltCondition {
		HashSet<Coordinate> candSet = layer.getViewer().getDivisibleSites();
		candidates = candSet.toArray(new Coordinate[0]);
		
		if (gs != null) {
			gs.add(getID(), candidates.length, candidates.length * 1.0D);
		}
	}
	
	public void fire(StepState state) throws HaltCondition {
		execute(state, candidates);
		candidates = null;
	}

}
