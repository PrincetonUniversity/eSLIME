package continuum.operations;

import java.util.Map;

import structural.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.sparse.CompDiagMatrix;

public abstract class ContinuumOperation extends CompDiagMatrix {

	protected Geometry geometry;
	private boolean useBoundaries;
	
	protected Coordinate[] sites;
	/**
	 * Construct an n x n matrix and populate it according to the connectivity
	 * of the geometry.
	 * 
	 * @param geometry
	 */
	public ContinuumOperation(Geometry geometry, boolean useBoundaries) {
		super(geometry.getCanonicalSites().length, geometry.getCanonicalSites().length);
		this.geometry = geometry;
		this.useBoundaries = useBoundaries;
		sites = geometry.getCanonicalSites();
	}

	public abstract void init();
	
	protected int mode() {
		if (useBoundaries) {
			return Geometry.APPLY_BOUNDARIES;
		} else {
			return Geometry.IGNORE_BOUNDARIES;
		}
	}
	
	protected int[] neighbors(Coordinate coord) {
		Coordinate[] neighborCoords = geometry.getNeighbors(coord,  mode());
		int[] neighbors = new int[neighborCoords.length];
		
		for (int i = 0; i < neighborCoords.length; i++) {
			neighbors[i] = geometry.coordToIndex(neighborCoords[i]);
		}
		
		return neighbors;
	}
	
	protected int connectivity() {
		return geometry.getConnectivity();
	}
	
	protected int dimension() {
		return geometry.getDimensionality();
	}
	
	
}
