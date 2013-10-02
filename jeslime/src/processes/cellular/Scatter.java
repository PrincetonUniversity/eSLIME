package processes.cellular;

import geometries.Geometry;
import io.project.CellFactory;
import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.dom4j.Element;

import processes.StepState;


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
	
	public Scatter(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);

		random = p.getRandom();
		
		numGroups = Integer.valueOf(get("types"));
		groupSize = Integer.valueOf(get("tokens"));
	}

	public void iterate(StepState state) throws HaltCondition {

		// Construct initial set of candidates
		HashSet<Coordinate> candidates = new HashSet<Coordinate>();
		
		for (Coordinate c : activeSites) {
			if (!lattice.getOccupiedSites().contains(c)) {
				candidates.add(c);
			}
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
				state.highlight(target);
				candidates.remove(target);
			}
		}

	}
}
