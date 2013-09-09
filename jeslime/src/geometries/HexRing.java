/**
 *
 * HexRing -- a 2D triangular lattice geometry with a rectangular shape, having
 * a periodic boundary condition in the horizontal dimension and a "hard"
 * boundary on the vertical dimension (i.e., reflective for solutes and no-flow
 * for cells.)
 *
 */
package geometries;

import java.util.ArrayList;

import structural.Flags;
import structural.identifiers.Coordinate;

public class HexRing extends Geometry {
	/*
	public:
		// System height and width
		HexRing(int,int);

		Coordinate wrap(int, int, int);
		bool isBeyondBounds(int, int);
		std::vector<Coordinate> getCellNeighbors(Coordinate);
		std::vector<Coordinate> getSoluteNeighbors(Coordinate);
		std::vector<Coordinate> getAnnulus(Coordinate, int, bool);
		std::vector<Coordinate> getCanonicalSites();
		std::vector<int> getDisplacement(Coordinate, Coordinate);

		double getL2Distance(Coordinate, Coordinate);

		Coordinate rel2abs(Coordinate, std::vector<int>);

	private:
		int height, width;

		// Get (naive) size of an annulus of the specified L1 radius from a 
		// point, i.e., size assuming no out-of-bounds points.
		int getAnnulusSize_(int);

		// True iff the coordinate is unwrappably out of bounds.
		bool isBeyondBounds_(int, int);

		// Evaluates whether or not to include a point in an annulus.
		void annulusJudge(int, int, int, Coordinate, bool, std::vector<Coordinate>&);

		// Convenience 2D wrap wrapper
		Coordinate wrap2(int, int);

		// Signum operator.
		int sign_(int);

		// Check to see if a coordinate should be rejected form an annulus on
		// circumnavigation grounds.
		bool circumnavigated_(const bool, Coordinate, Coordinate,
									   const int, const int, const int);
									   */

	private int height;
	private int width;

	public HexRing(int height, int width) {
		if (height % 2 != 0 || width % 2 != 0) {
			throw new IllegalArgumentException("Odd-sized linear dimension is not yet supported in HexRing.");
		}

		if (height <= 0 || width <= 0) {
			throw new IllegalArgumentException("Linear dimension must be positive.");
		}

		this.height = height;
		this.width = width;
	}

	public Coordinate wrap(int x0, int y0, int z0) {

		if (z0 != 0) {
			throw new IllegalStateException("Attempted to index non-zero 'z' coordinate in a 2D geometry.");
		}

		int flags = Flags.PLANAR;

		if (isBeyondBounds(x0, y0)) {
			flags |= Flags.BEYOND_BOUNDS;
		}

		// Adjust x
		int x = x0;
		while (x >= width) {
			x -= width;
		}

		while (x < 0) {
			x += width;
		}

		int y = y0;

		// Remove old x-adjustment for y
		if (x0 >= 0) {
			y -= (x0 / 2);
		} else {
			y -= ((x0-1)/2);
		}

		// Put in a new x-adjustment based on wrapped x
		y += (x / 2);

		// Index the new value
		Coordinate wrapped = new Coordinate(x, y, flags);
		return(wrapped);
	}

	Coordinate wrap2(int x, int y) {
		Coordinate res = wrap(x, y, 0);
		//std::// << "   in wrap2: (" << x << ", " << y << ") -> (" << fx(res) << ", " << fy(res) << ")" << std::endl;
		return res;
	}

	/**
	 * All x coordinates are legal (wrappable) but y coordinates can
	 * be out of bounds.
	 */
	boolean isBeyondBounds(int x, int y) {
		
		// Remove y adjustment
		int yAdj = y;

		// Remove old x-adjustment for y
		if (x >= 0) {
			yAdj -= (x / 2);
		} else {
			yAdj -= ((x-1)/2);
		}

		if (yAdj < 0)
			return(true);
			
		if (yAdj >= height)
			return(true);
			
		return false;			
	}

	public Coordinate[] getCellNeighbors(Coordinate coord) {
		// Use reserve/add because we don't know how big it will be.
		// Could also set null entries to -1, then use the same code throughout
		ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>(6);

		int x = coord.x();
		int y = coord.y();

		// North
		Coordinate candidate = wrap2(x, y+1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		// Northeast
		candidate = wrap2(x+1, y+1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		// Southeast
		candidate = wrap2(x+1, y);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		// South
		candidate = wrap2(x, y-1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		// Southwest
		candidate = wrap2(x-1, y-1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		// Northwest
		candidate = wrap2(x-1, y);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		}

		return(neighbors.toArray(new Coordinate[0]));
	}

	public Coordinate[] getSoluteNeighbors(Coordinate coord) {
		// Vertical reflection is easy because y values don't
		// affect x. So if (x, y+1) is too high, we can just use
		// (x, y) to reflect. Similarly, (x, y-1) becomes (x, y)
		// as well if it went off the edge.

		// We know there will always be six neighbors, so use
		// vector constructor (fastest)
		ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>(6);

		int x = coord.x();
		int y = coord.y();

		//int n = 0;

		// North
		Coordinate candidate = wrap2(x, y+1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		} else {
			candidate = wrap2(x, y);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
		}
		
		// Northeast
		candidate = wrap2(x+1, y+1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		} else {
			candidate = wrap2(x+1,y);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
		}

		// Southeast
		candidate = wrap2(x+1, y);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		} else {
			candidate = wrap2(x+1,y+1);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
		}

		// South
		candidate = wrap2(x, y-1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
		} else {
			candidate = wrap2(x, y);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
		}

		// Southwest
		candidate = wrap2(x-1, y-1);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
			//neighbors[n]	= candidate;
			//n++;
		} else {
			candidate = wrap2(x-1,y);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
			//neighbors[n]	= candidate;
			//n++;
		}

		// Northwest
		candidate = wrap2(x-1, y);
		if (!candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
			neighbors.add(candidate);
			//neighbors[n]	= candidate;
			//n++;
		} else {
			candidate = wrap2(x-1,y-1);
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				throw new IllegalStateException("Consistency error -- illegal reflection");
			}
			neighbors.add(candidate);
			//neighbors[n]	= candidate;
			//n++;
		}

		return(neighbors.toArray(new Coordinate[0]));
	}

	public Coordinate[] getCanonicalSites() {
		Coordinate[] sites = new Coordinate[height * width];

		int i = 0;
		//std::cout << "In getCanonicalSites: width=" << width << " and height=" << height << std::endl;
		for (int x = 0; x < width; x++) {
			int yBase = x / 2;
			
			for (int y = yBase; y < height + yBase; y++) {
				Coordinate idx = new Coordinate(x, y, 0);
				sites[i] = idx;
				
				i++;
			}
		}
		return sites;
	}

	public Coordinate rel2abs(Coordinate origin, int[] displacement) {
		if (displacement.length != 3) {
			throw new IllegalArgumentException("Expected three arguments to rel2abs(...)");
		}

		int x = origin.x();
		int y = origin.y();

		// Apply u component
		x += displacement[0];
		y += displacement[0];

		// Apply v component
		y += displacement[1];

		// Apply w component
		x += displacement[2];

		Coordinate target = wrap2(x, y);

		return target;
	}

	public int[] getDisplacement(Coordinate pCoord, Coordinate qCoord) {
		int[] displacement = new int[3];

		//std::cout << "In getDisplacement[" << stringForm(pCoord) << ", " << stringForm(qCoord) << "]" << std::endl;

		// Extract p coordinate
		int xp = pCoord.x();
		int yp = pCoord.y();

		// Extract q coordinate
		int xq = qCoord.x();
		int yq = qCoord.y();

		// If adjusted x delta exceeds half of the system width, get unwrapped
		// equivalent in the other direction
		int dx = xq - xp;
		if (Math.abs(dx) > (width / 2)) {
			// Get rid of old x correction for yq
			yq -= xq / 2;

			// Calculate unwrapped dx (there's a more elegant way, but my brain
			// is refusing to do algebra right now)
			dx = -1 * (int) Math.signum(dx) * (width - Math.abs(dx));
			
			xq = xp + dx;

			// Since xq may now be negative, use the sign-safe x-correction for yq
			if (xq >= 0) {
				yq += (xq / 2);
			} else {
				yq += ((xq-1)/2);
			}
		}

		// Now that we have the correct horizontal displacement (albeit potentially
		// in an "improper" form), get the displacement vector.
		int dy = yq - yp;

		// A triangular lattice is formed by a non-orthogonal basis:
		//    u = [1, 1] --> northeast
		//    v = [0, 1] --> north
		//    w = [1, 0] --> southeast

		// Step 1: get u component.
		int du = 0;
		if (dx < 0 && dy < 0) {
			du = Math.max(dx, dy);
		}

		if (dx > 0 && dy > 0) {
			du = Math.min(dx, dy);
		}

		// Step 2: subtracting du from dx and dy, we get dw and dv respectively.
		int dv = dy - du;
		int dw = dx - du;

		// Populate the vector in the new basis -- done!
		displacement[0] = du;
		displacement[1] = dv;
		displacement[2] = dw;

		// SANITY CHECK -- verify that the inverse returns the same coordinate.
		// TODO: move this check to a test.
		if (!qCoord.equals(rel2abs(pCoord, displacement))) {
			StringBuilder msg = new StringBuilder();
			msg.append("Consistency failure: rel2abs should be the inverse of getDisplacement, but isn't.\n");
			msg.append("   Details: pCoord = ");
			msg.append(pCoord.stringForm());
			msg.append("\n");
			msg.append("            qCoord = ");
			msg.append(qCoord.stringForm());
			msg.append("\n");
			msg.append("            displacement = <");
			msg.append(displacement[0]);
			msg.append(", ");
			msg.append(displacement[1]);
			msg.append(", ");
			msg.append(displacement[2]);
			msg.append(">\n");
			msg.append("            Resolved qCoord = ");
			msg.append(rel2abs(pCoord, displacement).stringForm());
			msg.append("\n");
			
			throw new IllegalStateException(msg.toString());
		}

		return(displacement);
	}

	public Coordinate[] getAnnulus(Coordinate coord, int r, boolean circ) {

		// De-index coordinate.
		int x0 = coord.x();
		int y0 = coord.y();

		// Result might have fewer targets than maximum due to boundaries
		// and circumnavigation restrictions
		int n = getAnnulusSize(r);

		// r=0 case (a point)
		if (r == 0) {
			return new Coordinate[] { new Coordinate(x0, y0, 0)};
		}

		// All other cases
		ArrayList<Coordinate> ring = new ArrayList<Coordinate>(n);

		for (int k = 1; k <= r; k++) {
			// Right side
			annulusJudge(x0+r, y0+k-1, r, coord, circ, ring);

			// Lower right side
			annulusJudge(x0+r-k, y0-k, r, coord, circ, ring);

			// Lower left side
			annulusJudge(x0-k, y0-r, r, coord, circ, ring);

			// Left side
			annulusJudge(x0-r, y0-r+k, r, coord, circ, ring);

			// Upper left side
			annulusJudge(x0-r+k, y0+k, r, coord, circ, ring);

			// Upper right side
			annulusJudge(x0+k, y0+r, r, coord, circ, ring);
		}

		return(ring.toArray(new Coordinate[0]));
	}

	private void annulusJudge(int x, int y, int r, Coordinate coord, boolean circ, ArrayList<Coordinate> ring) {

		Coordinate candidate = wrap2(x, y);

			// Cells that are beyond bounds should be tossed.
			if (candidate.hasFlag(Flags.BEYOND_BOUNDS)) {
				return;
			}

			// By definition, annnulus candidates have an L1 distance of r from the
			// center cell. However, the candidate may wrap all the way around,
			// which is undesirable in some situations. To avoid this, the circ flag
			// throws away cells that wrap to a new location which is at or less than
			// the desired annulus radius.

			if (circumnavigated(circ, coord, candidate, x, y, r)) {
				return;
			}


			// Otherwise, the candidate is fine. Retain it. Note than when circ==false,
			// some cells will be double-counted. This is by design.
			ring.add(candidate);
	}

	private boolean circumnavigated(boolean circ, Coordinate coord, Coordinate candidate, int x, int y, int r) {

		// If we're not checking circumnavigation, always return false.
		if (!circ) {
			return false;
		}

		// If the candidate didn't wrap, it also couldn't have circumnavigated.
		if (candidate.x() == x && candidate.y() == y) {
			return false;
		}

		// Otherwise, check to see how far the target is from the center in the
		// x direction. If it has not circumnavigated, wrapping should put it
		// further away, so if it's the same distance or closer, it did.

		int old_dx = Math.abs(x - coord.x());
		int wrapped_dx = Math.abs(candidate.x() - coord.x());

		if (wrapped_dx <= old_dx) {
			return true;
		}

		// If this last condition wasn't true, it didn't circumnavigate.
		return false;
	}

	public double getL2Distance(Coordinate pCoord, Coordinate qCoord) {
		throw new UnsupportedOperationException("Not yet implemented! (Default behavior incorrect due to non-orthogonal basis.)");
		// To implement this, you need to note that actually the x and y coordinates will have
		// the correct displacement from one another (INCLUDING the x correction!), although
		// dy needs a correction factor of 0.5 depending on which row it was.
	}

	private int getAnnulusSize(int radius) {
		if (radius == 0) {
			return(1);
		}

		return(6 * radius);
	}
}