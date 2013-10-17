package processes.cellular;

import geometries.Geometry;
import io.project.CellFactory;
import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.dom4j.Element;

import processes.StepState;
import processes.gillespie.GillespieState;
import cells.Cell;
import cells.SimpleCell;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

public class Scatter extends CellProcess {
	
	private int numGroups;
	private int groupSize;
	
	private Random random;
	
	private HashSet<Coordinate> candidates = null;
	
	public Scatter(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);

		random = p.getRandom();
		
		numGroups = Integer.valueOf(get("types"));
		groupSize = Integer.valueOf(get("tokens"));
	}

	public void target(GillespieState gs) throws HaltCondition {
		// Construct initial set of candidates
		candidates = new HashSet<Coordinate>();
		
		for (Coordinate c : activeSites) {
			if (!lattice.getOccupiedSites().contains(c)) {
				candidates.add(c);
			}
		}
		
		gs.add(this.getID(), candidates.size(), candidates.size() * 1.0D);
	}
	public void fire(StepState state) throws HaltCondition {
		if (candidates == null) {
			throw new IllegalStateException("fire() invoked on scatter before target().");
		}

		for (int i = 0; i < numGroups; i++) {
			CellFactory factory = getCellFactory(lattice);
			
			// Create a cell factory for this group 
			for (int j = 0; j < groupSize; j++) {

				if (candidates.isEmpty()) {
					throw new LatticeFullEvent(lattice.getGillespie()); 
				}

				// Choose target randomly
				Coordinate[] cVec = candidates.toArray(new Coordinate[0]);
				
				int o = random.nextInt(cVec.length);
				Coordinate target = cVec[o];

				Cell cell = factory.instantiate();
				
				lattice.place(cell, target);
				//state.highlight(target);
				candidates.remove(target);
			}
		}
		
		// Make sure that a new target must be chosen prior to next invocation.
		candidates = null;

	}
}
