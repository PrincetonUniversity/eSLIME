package discrete.cellular;

import geometry.Geometry;
import io.project.CellFactory;
import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import discrete.StepState;
import discrete.gillespie.GillespieState;
import cells.Cell;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

public class Scatter extends CellProcess {
	
	private int numGroups;
	private int groupSize;
	
	private Random random;
	
	private HashSet<Coordinate> candidates = null;
	
	public Scatter(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, layer, id, geom, p);

		random = p.getRandom();
		
		numGroups = Integer.valueOf(get("types"));
		groupSize = Integer.valueOf(get("tokens"));
	}

	public void target(GillespieState gs) throws HaltCondition {
		// Construct initial set of candidates
		candidates = new HashSet<Coordinate>();
		
		for (Coordinate c : activeSites) {
			if (!layer.getViewer().isOccupied(c)) {
				candidates.add(c);
			}
		}
		
		if (gs != null) {
			gs.add(this.getID(), candidates.size(), candidates.size() * 1.0D);
		}
	}
	public void fire(StepState state) throws HaltCondition {
		if (candidates == null) {
			throw new IllegalStateException("fire() invoked on scatter before target().");
		}

		for (int i = 0; i < numGroups; i++) {
			CellFactory factory = getCellFactory(layer);
			
			// Create a cell factory for this group 
			for (int j = 0; j < groupSize; j++) {

				if (candidates.isEmpty()) {
					throw new LatticeFullEvent(state.getTime()); 
				}

				// Choose target randomly
				Coordinate[] cVec = candidates.toArray(new Coordinate[0]);
				
				int o = random.nextInt(cVec.length);
				Coordinate target = cVec[o];

				Cell cell = factory.instantiate();
				
				layer.getUpdateManager().place(cell, target);
				
				//state.highlight(target);
				candidates.remove(target);
			}
		}
		
		// Make sure that a new target must be chosen prior to next invocation.
		candidates = null;

	}
}
