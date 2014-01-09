package processes.discrete;

import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import processes.StepState;
import processes.gillespie.GillespieState;
import geometry.Geometry;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
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
	public NeighborSwap(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, layer, id, geom, p);
		
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
		
		layer.getUpdateManager().swap(target.p, target.q);
		
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
		Set<Coordinate> coords = layer.getViewer().getOccupiedSites();
		
		// For each occupied site...
		for(Coordinate coord : coords) {
		
			// See how many occupied neighbors it has
			Coordinate[] neighbors = geom.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
			
			// Add each possible swap as a candidate
			for (Coordinate neighbor : neighbors) {
				if (layer.getViewer().isOccupied(neighbor)) {
					SwapTuple sw = new SwapTuple(coord, neighbor);
					candidates.add(sw);
				}
			}
			
		}
		
		// Weight is defined in terms of number of swappable cells,
		// whereas event count is defined in terms of number of possible
		// swaps.
		if (gs != null) {
			gs.add(getID(), candidates.size(), coords.size() * 1.0D);
		}
	}
}
