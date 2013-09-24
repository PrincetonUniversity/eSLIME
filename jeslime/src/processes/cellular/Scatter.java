package processes.cellular;

import geometries.Geometry;
import io.parameters.ProcessLoader;

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

		Element e = loader.getProcess(id);
		numGroups = Integer.valueOf(get(e, "types"));
		groupSize = Integer.valueOf(get(e, "tokens"));
	}

	public void iterate(StepState state) throws HaltCondition {
		//System.out.println("In Scatter::iterate().");

		// Construct initial set of candidates
		Coordinate[] canonicals = lattice.getCanonicalSites();
		
		HashSet<Coordinate> candidates = new HashSet<Coordinate> (canonicals.length);
		
		for (Coordinate c : canonicals) {
			if (!lattice.getOccupiedSites().contains(c)) {
				candidates.add(c);
			}
		}
		
		for (int i = 0; i < numGroups; i++) {
			for (int j = 0; j < groupSize; j++) {

				// TODO: This should be a unified exception that gets passed up to
				//       the Simulator.
				if (candidates.isEmpty()) {
					throw new LatticeFullEvent(lattice.getGillespie()); 
				}

				// Choose target randomly
				Coordinate[] cVec = candidates.toArray(new Coordinate[0]);
				
				int o = random.nextInt(cVec.length);
				Coordinate target = cVec[o];

				// Create and assign cell
				Cell cell = new SimpleCell(i+1);

				lattice.place(cell, target);
				state.highlight(target);
				candidates.remove(target);
			}
		}

	}
}
