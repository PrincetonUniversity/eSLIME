package processes.cellular;

import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import processes.StepState;
import processes.gillespie.GillespieState;
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

	private List<SwapTuple> candidates = null;
	public NeighborSwap(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
		
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		
		if (candidates == null) {
			throw new IllegalStateException("Attempted to call fire() before calling target().");
		}
		
		if (candidates.size() == 0) {
			return;
		}		
		
		//System.out.println("In NeighborSwap::iterate().");

		SwapTuple target = selectTarget();
		
		if (target == null) {
			return;
		}
		
		lattice.swap(target.p, target.q);
		
		state.highlight(target.p);
		state.highlight(target.q);
		
		candidates = null;
	}

	private SwapTuple selectTarget() {

		
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

	@Override
	public void target(GillespieState gs) throws HaltCondition {
		
		// Create an ArrayList of SwapTuples
		candidates = new ArrayList<SwapTuple>();
		
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
		
		// Weight is defined in terms of number of swappable cells,
		// whereas event count is defined in terms of number of possible
		// swaps.
		gs.add(getID(), candidates.size(), coords.size() * 1.0D);
	}
}
