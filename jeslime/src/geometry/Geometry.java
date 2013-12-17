package geometry;

import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import no.uib.cipr.matrix.io.MatrixVectorReader;
import structural.Flags;
import structural.identifiers.Coordinate;

public class Geometry {

	/* Lookup modes */
	
	// Use boundary conditions to calculate "true" coordinate.
	public static final int APPLY_BOUNDARIES = 0;
	
	// Ignore boundary conditions; return coordinate as if infinite
	// boundary conditions existed. 
	public static final int IGNORE_BOUNDARIES = 1;
	
	// Remove any cells that are beyond boundaries from results.
	public static final int EXCLUDE_BOUNDARIES = 2;
	
	// Ignore boundary conditions, but indicate that they would have
	// applied.
	public static final int FLAG_BOUNDARIES = 3;
	
	private Lattice lattice;
	private Boundary boundary;
	private Shape shape;
	
	private HashMap<Coordinate, Integer> coordinateIndex = new HashMap<Coordinate, Integer>();

	public Geometry(Lattice lattice, Shape shape, Boundary boundary) {
		this.boundary = boundary;
		this.lattice = lattice;
		this.shape = shape;
		rebuildIndex();
	}
	
	public Coordinate apply(Coordinate coord, int mode) {
		if (mode == APPLY_BOUNDARIES) {
			return applyBoundaries(coord);
		} else if (mode == FLAG_BOUNDARIES) {
			return setBoundaryFlag(coord);
		} else if (mode == EXCLUDE_BOUNDARIES) {
			return excludeBoundaries(coord);
		} else if (mode == IGNORE_BOUNDARIES ){
			return coord;
		} else {
			throw new IllegalArgumentException("Unrecognized mode " + mode + ".");
		}
	}
	
	public Coordinate[] getNeighbors(Coordinate coord, int mode) {
		Coordinate[] neighbors = lattice.getNeighbors(coord);
		if (mode == APPLY_BOUNDARIES) {
			return applyBoundaries(neighbors);
		} else if (mode == FLAG_BOUNDARIES) {
			return setBoundaryFlag(neighbors);
		} else if (mode == EXCLUDE_BOUNDARIES) {
			return excludeBoundaries(neighbors);
		} else if (mode == IGNORE_BOUNDARIES ){
			return neighbors;
		} else {
			throw new IllegalArgumentException("Unrecognized mode " + mode + ".");
		}
	}
	
	public Coordinate[] getAnnulus(Coordinate coord, int r, int mode) {
		Coordinate[] annulus = lattice.getAnnulus(coord, r);
		if (mode == APPLY_BOUNDARIES) {
			return applyBoundaries(annulus);
		} else if (mode == FLAG_BOUNDARIES) {
			return setBoundaryFlag(annulus);
		} else if (mode == EXCLUDE_BOUNDARIES) {
			return excludeBoundaries(annulus);
		} else if (mode == IGNORE_BOUNDARIES ){
			return annulus;
		} else {
			throw new IllegalArgumentException("Unrecognized mode " + mode + ".");
		}
	}
	
	public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord, int mode) {
		
		if (mode == APPLY_BOUNDARIES) {
			Coordinate pw = boundary.apply(pCoord);
			Coordinate qw = boundary.apply(qCoord);
			Coordinate wrapped = lattice.getDisplacement(pw, qw);
			return wrapped;
		} else if (mode == FLAG_BOUNDARIES) {
			throw new UnsupportedOperationException("You need to convert the return type of getDisplacement to a coordinate vector before you can use FLAG_BOUNDARIES meaningfully for displacement.");
		} else if (mode == EXCLUDE_BOUNDARIES) {
			throw new UnsupportedOperationException("You need to convert the return type of getDisplacement to a coordinate vector before you can use EXCLUDE_BOUNDARIES meaningfully for displacement.");
		} else if (mode == IGNORE_BOUNDARIES ){
			Coordinate naive = lattice.getDisplacement(pCoord, qCoord);
			return naive;
		} else {
			throw new IllegalArgumentException("Unrecognized mode " + mode + ".");
		}
	}
	
	public Coordinate rel2abs(Coordinate coord, Coordinate displacement, int mode) {
		Coordinate naive = lattice.rel2abs(coord, displacement);
		
		if (mode == APPLY_BOUNDARIES) {
			// TODO: In functional test, important to make sure that out of bounds + displacement = correct wrap.
			return applyBoundaries(naive);
		} else if (mode == FLAG_BOUNDARIES) {
			return setBoundaryFlag(naive);
		} else if (mode == EXCLUDE_BOUNDARIES) {
			return excludeBoundaries(naive);
		} else if (mode == IGNORE_BOUNDARIES ){
			return naive;
		} else {
			throw new IllegalArgumentException("Unrecognized mode " + mode + ".");
		}
	}
		
	public int getL1Distance(Coordinate p, Coordinate q, int mode) {
		Coordinate vec = getDisplacement(p, q, mode);
		// Calculate L1 norm. If basis is only 2D, third component will be zero
		int res = Math.abs(vec.x()) + Math.abs(vec.y());
		
		if (!vec.hasFlag(Flags.PLANAR)) {
			res += Math.abs(vec.z());
		}
		
		return(res);
	}
	
	public Coordinate[] getCanonicalSites() {
		return shape.getCanonicalSites();
	}
	
	private Coordinate[] setBoundaryFlag(Coordinate[] coords) {
		ArrayList<Coordinate> applied = new ArrayList<Coordinate>(coords.length);
		
		for (Coordinate coord : coords) {
			applied.add(setBoundaryFlag(coord));
		}
		
		return applied.toArray(new Coordinate[0]);
		
	}
	
	private Coordinate[] applyBoundaries(Coordinate[] coords) {
		ArrayList<Coordinate> applied = new ArrayList<Coordinate>(coords.length);
		
		for (Coordinate coord : coords) {
			Coordinate res = applyBoundaries(coord);
			
			if (res != null) {
				applied.add(res);
			}
		}
		
		return applied.toArray(new Coordinate[0]);
	}
	
	private Coordinate[] excludeBoundaries(Coordinate[] coords) {
		ArrayList<Coordinate> applied = new ArrayList<Coordinate>(coords.length);
		
		for (Coordinate coord : coords) {
			Coordinate candidate = excludeBoundaries(coord);
			if (candidate != null) {
				applied.add(candidate);
			}
		}
		return applied.toArray(new Coordinate[0]);
	}
	
	private Coordinate setBoundaryFlag(Coordinate c) {
		Coordinate w = boundary.apply(c);

		if (!w.equals(c)) {
			int flags = c.flags() | Flags.BOUNDARY_IGNORED;
			
			if (c.hasFlag(Flags.PLANAR)) {
				return new Coordinate(c.x(), c.y(), flags);
			} else {
				return new Coordinate(c.x(), c.y(), c.z(), flags);
			}
		} else {
			return c;
		}
	}
	
	private Coordinate applyBoundaries(Coordinate c) {
		Coordinate wrapped = boundary.apply(c);
		return wrapped;
	}
	
	private Coordinate excludeBoundaries(Coordinate c) {
		Coordinate w = boundary.apply(c);
		if (w == null) {
			return null;
		} else if (!w.equals(c)) {
			return null;
		} else {
			return c;
		}
	}
	
	public int getDimensionality() {
		return lattice.getDimensionality();
	}
	
	public int getConnectivity() {
		return lattice.getConnectivity();
	}
	
	public boolean isInfinite() {
		return boundary.isInfinite();
	}
	
	public Integer coordToIndex(Coordinate coord) {
		Coordinate canonical = coord.canonicalize();

        if (!coordinateIndex.containsKey(canonical)) {
            return null;
        } else {
		    return coordinateIndex.get(canonical);
        }
	}
	
	public void rebuildIndex() {
		//System.out.println("In rebuildIndex. Coordinate index: " + coordinateIndex.size());
		coordinateIndex.clear();
		//System.out.println("   Cleared. Coordinate index: " + coordinateIndex.size());
		//System.out.println("   Rebuilding.");
	
		
		// dependencies are sometimes left uninitialized for mock testing.
		// In these cases, there is nothing to index, so return.
		if (getCanonicalSites() == null) {
			//System.out.println("      canonicalSites is null; returning.");
			return;
		}
		
		Coordinate[] sites = getCanonicalSites();
	
		
		for (Integer i = 0; i < sites.length; i++) {
			Coordinate c = sites[i];
			
			// Coordinate index is for canonical coordinates only
			Coordinate cc = c.canonicalize();
			//System.out.println("      Adding " + c);
			coordinateIndex.put(cc, i);
		}
		//System.out.println("   Rebuild complete. Coordinate index: " + coordinateIndex.size());
		
	}

    public Coordinate getCenter() {
        return shape.getCenter();
    }
}
