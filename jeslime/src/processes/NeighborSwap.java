package processes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	private Geometry geom;
	private GeneralParameters p;
	public NeighborSwap(Lattice lattice, Geometry geom, GeneralParameters p) {
		super(lattice);
		
		this.geom = geom;
		this.p = p;
	}

	@Override
	public Coordinate[] iterate() throws HaltCondition {
		
		SwapTuple target = selectTarget();
		
		lattice.swap(target.p, target.q);
		
		return target.toArray();
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
		
		public Coordinate[] toArray() {
			Coordinate[] arr = new Coordinate[2];
			
			arr[0] = p;
			arr[1] = q;
			
			return arr;
		}
	}
}
