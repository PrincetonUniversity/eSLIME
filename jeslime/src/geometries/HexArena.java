/**
  * HexArena -- a 2D triangular lattice with a rectangular shape, having
  * absorbing boundary conditions on all sides for both cells and solutes.
  *
  * Cells which go beyond bounds return ESLIME::END_OF_WORLD.
  */
package geometries;

import java.util.ArrayList;

import structural.Flags;
import structural.identifiers.Coordinate;

public class HexArena extends Geometry {
	private int height;
	private int width;
	
		public HexArena(int height, int width) {
			if (height % 2 != 0 || width % 2 != 0) {
				throw new IllegalArgumentException("Odd-sized linear dimension is not yet supported in HexArena.");
			}

			if (height <= 0 || width <= 0) {
				throw new IllegalArgumentException("Linear dimension must be positive.");
			}

			this.height = height;
			this.width = width;
		}
	
		public Coordinate wrap(int x0, int y0, int z0) {

			if (z0 != 0) {
				throw new IllegalStateException("Illegal state exception: attempted to index non-zero 'z' coordinate in a 2D geometry.");
			}

			// Anything out of bounds falls off edge of world
			int flags = 0;
			
			if (isOutOfThisWorld(x0, y0)) {
				flags |= Flags.END_OF_WORLD;
			}

			// There's no wrapping in this geometry
			return new Coordinate(x0, y0, flags);		}
		
		/**
		 * Cell neighbors and solute neighbors are exactly the same -- near the
		 * boundary, some of the neighbors display the END_OF_WORLD flag. Solute
		 * solvers will generally treat this as an absorbing condition, and cell
		 * transforms will similarly treat it as a signal to delete the cell from
		 * the system.
		 *
		 * @brief HexArena::getCellNeighbors
		 * @param coord
		 * @return
		 */
		public Coordinate[] getCellNeighbors(Coordinate coord) {
			// Use reserve/add because we don't know how big it will be.
			// Could also set null entries to -1, then use the same code throughout
			Coordinate[] neighbors = new Coordinate[6];

			int x = coord.x();
			int y = coord.y();

			Coordinate candidate;

			// We don't need to do any checking because every coordinate is "legal."
			// The wrapper will return the end of world flag if applicable.

			// North
			candidate = wrap2(x, y+1);
			neighbors[0] 	= candidate;

			// Northeast
			candidate = wrap2(x+1, y+1);
			neighbors[1] 	= candidate;

			// Southeast
			candidate = wrap2(x+1, y);
			neighbors[2] 	= candidate;

			candidate = wrap2(x, y-1);
			neighbors[3] 	= candidate;

			// Southwest
			candidate = wrap2(x-1, y-1);
			neighbors[4] 	= candidate;

			// Northwest
			candidate = wrap2(x-1, y);
			neighbors[5] 	= candidate;

			return neighbors;
		}
		
		/**
		 * @brief HexArena::getSoluteNeighbors
		 * @see HexArena::getCellNeighbors(const int);
		 *
		 * Identical to getCellNeighbors(...).
		 */
		public Coordinate[] getSoluteNeighbors(Coordinate coord) {
			return getCellNeighbors(coord);
		}
		
		public Coordinate[] getAnnulus(Coordinate coord, int r, boolean circ) {

			// Note: circ has no effect in HexArena, since there is no wrapping

			// De-index coordinate.
			int x0 = coord.x();
			int y0 = coord.y();

			// Result might have fewer targets than maximum due to boundaries
			// and circumnavigation restrictions
			int n = getAnnulusSize(r);

			// r=0 case (a point)
			if (r == 0) {
				return new Coordinate[] {new Coordinate(x0, y0, 0)};
			}
			
			// All other cases
			ArrayList<Coordinate> ring = new ArrayList<Coordinate>(n);
			
			for (int k = 1; k <= r; k++) {
				// Right side
				Coordinate candidate = wrap2(x0+r, y0+k-1);
				ring.add(candidate);

				// Lower right side
				candidate = wrap2(x0+r-k, y0-k);
				ring.add(candidate);

				// Lower left side
				candidate = wrap2(x0-k, y0-r);
				ring.add(candidate);

				// Left side
				candidate = wrap2(x0-r, y0-r+k);
				ring.add(candidate);

				// Upper left side
				candidate = wrap2(x0-r+k, y0+k);
				ring.add(candidate);

				// Upper right side
				candidate = wrap2(x0+k, y0+r);
				ring.add(candidate);
			}

			return(ring.toArray(new Coordinate[0]));
		}
		
		public Coordinate[] getCanonicalSites() {
			int n = height * width;
			Coordinate[] sites = new Coordinate[n];
			
			int i = 0;

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
		
		public int[] getDisplacement(Coordinate p, Coordinate q) {
			int[] displacement = new int[3];

			// Extract p coordinate
			int xp = p.x();
			int yp = p.y();

			// Extract q coordinate
			int xq = q.x();
			int yq = q.y();

			int dx = xq - xp;
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

			return(displacement);
		}
		
		public double getL2Distance(Coordinate p, Coordinate q) {
			throw new UnsupportedOperationException("Not yet implemented! (Default behavior incorrect due to non-orthogonal basis.)");
			// To implement this, you need to note that actually the x and y coordinates will have
			// the correct displacement from one another (INCLUDING the x correction!), although
			// dy needs a correction factor of 0.5 depending on which row it was.
		}
		
		public Coordinate rel2abs(Coordinate origin, int[] displacement) {
			if (displacement.length != 3) {
				throw new IllegalArgumentException("Expected three arguments to HexArena::rel2abs(...)");
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
		

		// Get (naive) size of an annulus of the specified L1 radius from a
		// point, i.e., size assuming no out-of-bounds points.
		private int getAnnulusSize(int radius) {
			if (radius == 0) {
				return(1);
			}

			return(6 * radius);
		}

		/**
		 * Anything that exceeds the linear dimensions of the system is off the edge of
		 * the world and will be removed.
		 */
		private boolean isOutOfThisWorld(int x, int y)  {

			// X out of bounds?
			if (x < 0) {
				return true;
			}

			if (x >= width) {
				return true;
			}

			// If not, remove y adjustment to check Y
			int yAdj = y;

			// Remove old x-adjustment for y
			if (x >= 0) {
				yAdj -= (x / 2);
			} else {
				yAdj -= ((x-1)/2);
			}

			// Y out of bounds?
			if (yAdj < 0)
				return(true);

			if (yAdj >= height)
				return(true);

			return false;
		}

		// Convenience 2D wrap wrapper
		private Coordinate wrap2(int x, int y) {
			Coordinate res = wrap(x, y, 0);
			return res;
		}
}
