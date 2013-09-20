package geometries;

import java.util.Vector;

import no.uib.cipr.matrix.io.MatrixVectorReader;

import structural.identifiers.Coordinate;

public abstract class Geometry {

	/*******************************
	 * Geometry-specific functions *
	 *******************************/

	// Wrap and index a coordinate. If geometry is 2D, should throw an
	// error for non-zero "z" entries.
	public abstract Coordinate wrap(int x0, int y0, int z0);

	// Returns an array of legal cellular neighbors for the specified
	// coordinate. At edges or corners, this may be fewer than the
	// maximum (depending on geometry). Order is guaranteed to be
	// preserved, such that the first half of the neighbor list should
	// be positive basis vectors and and the second half negative basis
	// vectors.
	public abstract Coordinate[] getCellNeighbors(Coordinate coord);

	// Returns an array of legal solute neighbors for the specified
	// coordinate. At edges or corners, this may include the cell
	// itself and/or the same neighbor multiple times. 
	public abstract Coordinate[] getSoluteNeighbors(Coordinate coord);

	// Returns an array of cells in the annulus at L1 distance "r"
	// from the specified coordinate. If circumnavigate=false, any
	// entry that would have a closer L1 distance without wrapping
	// will be excluded. Order is NOT guaranteed to be preserved!
	public abstract Coordinate[] getAnnulus(Coordinate coord, int r, boolean circ);

	// Get a complete list of sites for this geometry.
	public abstract Coordinate[] getCanonicalSites();

	// Get the 3D displacement vector between the specified sites. The
	// vector is in the natural basis of the geometry--i.e., a 2D hex
	// lattice would still have a 3D displacement vector. If the basis
	// is 2D, the z-component will simply be 0. In periodic geometries,
	// the shortest basis vector should be returned; otherwise, this
	// method ignores boundaries.
	public abstract int[] getDisplacement(Coordinate pCoord, Coordinate qCoord);

    // Given a coordinate and a displacement vector, give an absolute
    // coordinate or appropriate exception code for the current geometry.
    // Wraps the coordinate if appropriate for the geometry.
	public abstract Coordinate rel2abs(Coordinate coord, int[] displacement);

	// Get the L1 distance between the specified sites.
	public int getL1Distance(Coordinate p, Coordinate q) {

		// Get displacement vector
		int[] vec = getDisplacement(p, q);

		// Calculate L1 norm. If basis is only 2D, third component will be zero.
		int res = Math.abs(vec[0]) + Math.abs(vec[1]) + Math.abs(vec[2]);

		return(res);
	}

	// Get the L2 distance between the specified sites.
	public double getL2Distance(Coordinate p, Coordinate q) {
		// Get displacement vector
		int[] fVec = getDisplacement(p, q);

		// L2 norm	
		double squared = Math.pow(fVec[0], 2.0) + Math.pow(fVec[1], 2.0) + Math.pow(fVec[2], 2.0);
	 
		double res = Math.sqrt(squared);

		return(res);
	}

	/**
	 * Returns the number of canonical sites in the system. Should be O(1).
	 * @return
	 */
	public abstract int getSiteCount();

	/**
	 * If false, we are allowed to check against the size of the canonical site
	 * array to determine the number of legal lattice sites. If false, we should
	 * treat the number of legal lattice sites as infinite. 
	 * 
	 * Note that, if an infinite number of sites is promised, this promise must
	 * be kept (for annulus, etc.) or else jeSLIME is likely to enter infinite
	 * loop conditions and hang.
	 * 
	 * @return
	 */
	public abstract boolean isInfinite();
}
