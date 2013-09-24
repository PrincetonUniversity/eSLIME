package processes.cellular;

import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import processes.StepState;


import geometries.Geometry;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Neighbor swap -- switches a randomly chosen cell with one of
 * its neighbors.
 * 
 * @author dbborens
 *
 */
public class NeighborSwap extends CellProcess {

	public NeighborSwap(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
		
	}

	@Override
	public void iterate(StepState state) throws HaltCondition {
		//System.out.println("In NeighborSwap::iterate().");

		SwapTuple target = selectTarget();
		
		lattice.swap(target.p, target.q);
		
		state.highlight(target.p);
		state.highlight(target.q);
	}

	private SwapTuple selectTarget() {
		// Create an ArrayList of SwapTuples
		List<SwapTuple> candidates = new ArrayList<SwapTuple>();
		
		// Get a list of occupied sites
		Set<Coordinate> coords = lattice.getOccupiedSites();
		
		// For each occupied site...
		for(Coordinate coord : coords) {
		
			// See how many occupied neighbors it has
			Coordinate[] neighbors = geom.getCellNeighbors(coord);
			
			// Add each possible swap as a candidate
			for (Coordinate neighbor : neighbors) {
				if (lattice.isOccupied(neighbor)) {
					SwapTuple sw = new SwapTuple(coord, neighbor);
					candidates.add(sw);
				}
			}
			
		}
		
		// Choose a candidate
		int index = p.getRandom().nextInt(candidates.size());
		SwapTuple target = candidates.get(index);
		return target;
	}

	/**
	 * Convenience class for pairs of coordinates to swap.
	 * 
	 */
	private class SwapTuple {
		public Coordinate p;
		public Coordinate q;
		
		public SwapTuple(Coordinate p, Coordinate q) {
			this.p = p;
			this.q = q;
		}
	}
}
