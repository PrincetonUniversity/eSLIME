package continuum.operations;

import java.util.HashMap;
import java.util.Map;

import structural.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.sparse.CompDiagMatrix;

public abstract class ContinuumOperation extends CompDiagMatrix {

	protected Geometry geometry;
	
	// Reverse lookup map of coordinate to coordinate index
	protected Map<Coordinate, Integer> coordToIndex;
	
	protected Coordinate[] sites;
	/**
	 * Construct an n x n matrix and populate it according to the connectivity
	 * of the geometry.
	 * 
	 * @param geometry
	 */
	public ContinuumOperation(Geometry geometry) {
		super(geometry.getCanonicalSites().length, geometry.getCanonicalSites().length);
		this.geometry = geometry;
		sites = geometry.getCanonicalSites();
	}

	protected abstract void buildMatrix();
	
}
