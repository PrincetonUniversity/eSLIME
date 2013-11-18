package geometry.lattice;

import structural.Flags;
import structural.identifiers.Coordinate;

public abstract class Lattice {
	
	protected Coordinate[] basis;
	
	public Lattice() {
		defineBasis();
	}
	
	/**
	 * Populate the array of basis coordinates that is supplied
	 * on calls to getBasisNeighbors().
	 */
	protected abstract void defineBasis();
	
	/**
	 * The number of spatial dimensions of the basis vector for the lattice.
	 * Not all geometries use orthogonal bases; for example, a triangular
	 * 2D lattice has dimensionality 2 and connectivity 3.
	 */
	public abstract int getConnectivity();
	
	/**
	 * The number of spatial dimensions of the lattice.
	 * @return
	 */
	public abstract int getDimensionality();
	
	/**
	 * The native coordinate system for eSLIME is Cartesian. That is,
	 * if we specify a rectangular geometry with width 10 and height 5,
	 * we expect this to be reflected as (roughly) a rectangle-shaped
	 * lattice, regardless of the lattice connectivity. 
	 * 
	 * However, some
	 * lattices do not have orthogonal bases. To make them consistent
	 * with a Cartesian coordinate system, it is necessary to adjust an
	 * incoming Cartesian coordinate to reflect their native coordinate
	 * system. That adjustment is supplied in the adjust(...) method.
	 * 
	 * @param toAdjust -- a Cartesian coordinate.
	 * @return -- a coordinate in the native coordinate system.
	 */
	public abstract Coordinate adjust(Coordinate toAdjust);
	
	/**
	 * invAdjust -- inverse operation to Adjust.
	 * 
	 * @param toAdjust
	 * @return
	 */
	public abstract Coordinate invAdjust(Coordinate toAdjust);
	
	/**
	 * Returns coordinates representing the neighbor offset by +1 in
	 * each basis direction. The number of basis neighbors is equal 
	 * to the connectivity, not the dimensionality, of the geometry.
	 * 
	 * @return
	 */
	public Coordinate[] getBasis() {
		return basis;
	}
	
	/**
	 * Returns the neighbors around the specified coordinate, assuming
	 * infinite boundary conditions.
	 * 
	 * @param coord
	 * @return
	 */
	public Coordinate[] getNeighbors(Coordinate coord) {
		return getAnnulus(coord, 1);
	}
	
	/**
	 * Returns the annulus of radius r around the specified coordinate,
	 * assuming infinite boundary conditions.
	 */
	public abstract Coordinate[] getAnnulus(Coordinate coord, int r);
	
	/**
	 * Get the displacement vector between the specified coordinates. The
	 * displacement vector is given in units of the lattice's basis vector.
	 * The displacement assumes infinite boundary conditions.
	 * 
	 * In the event that the basis of the lattice is linearly dependent, as in
	 * the case of the triangular lattice, the displacement vector returned
	 * by this method should minimize the L1 distance. Linear independence of
	 * components can be enforced using getOrthoDisplacement().
	 */
	public abstract Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord);
	
	/**
	 * Get a displacement vector for the specified coordinates. If basis vectors
	 * are non-orthogonal, getOrthoIDisplacement() returns a displacement
	 * vector such that all non-zero components are orthogonal. For many 
	 * geometries, this is identical to getDisplacement().
	 * 
	 * @param pCoord
	 * @param qCoord
	 * @return
	 */
	public abstract Coordinate getOrthoDisplacement(Coordinate pCoord, 
			Coordinate qCoord);
	
	/**
	 * Traverses the specified displacement from a given coordinate, and
	 * gives the resulting location back as a new coordinate.
	 */
	public abstract Coordinate rel2abs(Coordinate coord, Coordinate displacement);
	
	// Get the L1 distance between the specified sites.
	public int getL1Distance(Coordinate p, Coordinate q) {

		// Get displacement vector
		Coordinate vec = getDisplacement(p, q);

		// Calculate L1 norm. If basis is only 2D, third component will be zero
		int res = Math.abs(vec.x()) + Math.abs(vec.y());
		
		if (!vec.hasFlag(Flags.PLANAR)) {
			res += Math.abs(vec.z());
		}
		
		return(res);
	}
}
