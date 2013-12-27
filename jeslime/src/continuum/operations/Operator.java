package continuum.operations;

import java.util.Map;

import org.dom4j.Element;

import structural.EpsilonUtil;
import structural.MatrixUtils;
import structural.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.sparse.CompDiagMatrix;

public abstract class Operator extends CompDiagMatrix {

	protected Geometry geometry;

	protected boolean useBoundaries;
	
	protected Coordinate[] sites;
	/**
	 * Construct an n x n matrix and populate it according to the connectivity
	 * of the geometry.
	 * 
	 * @param geometry -- the geometry upon which to base the operator.
	 * @param useBoundaries -- utilize boundary conditions in constructing
	 *     the operator. WARNING: disabling boundary conditions can lead to
	 *     illegal coordinate indices!
	 */
	public Operator(Geometry geometry, boolean useBoundaries) {
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
			return Geometry.EXCLUDE_BOUNDARIES;
		}
	}
	
	protected int[] neighbors(Coordinate coord) {
        //System.out.println("Operator::neighbors --> geometry is null? " + (geometry == null));
		//System.out.println("In neighbors(" + coord + ")");
		Coordinate[] neighborCoords = geometry.getNeighbors(coord,  mode());
        //System.out.print("Getting neighbors for coordinate " + coord);
		int[] neighbors = new int[neighborCoords.length];
		//System.out.println("  --> found " + neighbors.length);
		//System.out.println("   Neighbor set: ");
		//for (int i = 0; i < neighborCoords.length; i++) {
		//	System.out.println("      " + neighborCoords[i]);
		//}
		for (int i = 0; i < neighborCoords.length; i++) {
            //System.out.println("   " + neighborCoords[i]);
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
	
	public boolean isUseBoundaries() {
		return useBoundaries;
	}

	protected void augment(int i, int j, double delta) {
		// Skip the operation to avoid initializing unnecessary
		// data structures if the magnitude of the delta  is 
		// below machine epsilon
		if (Math.abs(delta) < EpsilonUtil.epsilon()) {
			return;
		}
		
		double current = get(i, j);
		set(i, j, current + delta);
	}


    @Override
    public String toString() {
        return MatrixUtils.matrixForm(this);
    }
}
