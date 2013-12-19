package geometry.shape;

import java.util.*;

import org.dom4j.Element;

import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * A hexagon-shaped arena. This is a strange geometry,
 * in that it has a number of sides equal to its connectivity,
 * rather than its dimension, and in that its coordinate
 * system is best thought of in terms of the non-orthogonal
 * basis vectors of the triangular lattice. For this reason,
 * it overrides many functions.
 * 
 * @author dbborens
 *
 */
public class Hexagon extends Shape {

	private int radius;
	
	public Hexagon(Lattice lattice, int radius) {
		super(lattice);
		this.radius = radius;
		init();
	}

	public Hexagon(Lattice lattice, Element descriptor) {
		super(lattice);
		
		radius = Integer.valueOf(descriptor.element("radius").getTextTrim());

		init();
	}
	
	@Override
	protected void verify(Lattice lattice) {
		if (!(lattice instanceof TriangularLattice)) {
			throw new IllegalArgumentException("Hexagonal geometry shape requires a triangular lattice.");
		}
	}

	@Override
	public Coordinate getCenter() {
        // This geometry is defined around a center coordinate of (r, r).
        // It is built up as a ring around this coordinate.
		Coordinate center = new Coordinate(radius, radius, 0);
		return center;
	}

	@Override
	public Coordinate[] getBoundaries() {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		ring(coords, radius);
		
		return coords.toArray(new Coordinate[0]);
	}

	@Override
	protected Coordinate[] calcSites() {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for (int r = 0; r <= radius; r++) {
			ring(coords, r);
		}
		
		return coords.toArray(new Coordinate[0]);
	}
	
	private void ring(ArrayList<Coordinate> coords, int r) {
		if (r == 0) {
			include(coords, getCenter());
			return;
		}
	
		int x0 = getCenter().x();
		int y0 = getCenter().y();
		for (int k = 1; k <= r; k++) {
			// Right side
			include(coords, new Coordinate(x0+r, y0+k-1, 0));

			// Lower right side
			include(coords, new Coordinate(x0+r-k, y0-k, 0));

			// Lower left side
			include(coords, new Coordinate(x0-k, y0-r, 0));

			// Left side
			include(coords, new Coordinate(x0-r, y0-r+k, 0));

			// Upper left side
			include(coords, new Coordinate(x0-r+k, y0+k, 0));

			// Upper right side
			include(coords, new Coordinate(x0+k, y0+r, 0));
		}
	}


	
	@Override
	protected void include(Collection<Coordinate> list, Coordinate coordinate) {
		list.add(coordinate);		
	}


	@Override
    /**
     * Get extent overbound of the specified coordinate, if any.
     *
     * The getOverbounds method for Hexagon is degenerate in that there are
     * ambiguous cases. For example, consider the case of (0, 4) with an r=2
     * hexagon. This could be considered +2w from the corner coordinate (0, 2)
     * or -2u from the corner coordinate (2, 4). In this case, we favor the use
     * of the "true" basis vector <u, w> and therefore prefer the latter.
     *
     */
	public Coordinate getOverbounds(Coordinate coord) {
		// Get natural-basis displacement from center
		Coordinate origin = getCenter();
		Coordinate d = lattice.getDisplacement(origin, coord);
		
		// Remember that diameter of Hexagon geometry is 2r + 1,
		// so out of bounds is r > R (strictly greater)

        int[] overages = new int[] {0, 0, 0};
        // Turn original displacement vector into an array.
        int[] dArr = new int[] {d.x(), d.y(), d.z()};

        // Step 1: Look for simple overages.
        for (int i = 0; i < 3; i++) {
            int overage = getOverage(dArr[i]);
            overages[i] = overage;
            dArr[i] -= overage;
        }

        // Step 2: While |d| exceeds the radius, eliminate the shortest
        // vector components.
        while(norm(dArr) > radius) {

            // Find minimum non-zero magnitude (mnz) of the remaining offsets.
            int mnz = mnz(dArr);

            // If there's a tie, it has to be between an true basis
            // direction (u or w) and the degenerate "basis" v. Therefore,
            // in case of tie, consider only u or w, with u preferred.

            // If u == w, then something went wrong, because this is the
            // definition of v.
            if (dArr[0] == dArr[2]) {
                throw new IllegalStateException("Geometry expectation violated in Hexagon::getOverbounds.");
            }

            // Case 1. |du| > 0 and mnz(d) = |du|.
            if (dArr[0] != 0 && a(dArr[0]) == mnz) {
                overages[0] += dArr[0];
                dArr[0] = 0;
                continue;

            // Case 2. |dw| > 0 and min(d) = dw.
            } else if (dArr[2] != 0 && a(dArr[2]) == mnz) {
                overages[2] += dArr[2];
                dArr[2] = 0;
                continue;

            // Case 3. |dv| > 0 and min(d) = dv.
            } else if (dArr[1] != 0 && a(dArr[1]) == mnz) {
                overages[1] += dArr[1];
                dArr[1] = 0;
                continue;
            }

            // Else, something went wrong.
            throw new IllegalStateException("Undefined state in Hexagon::getOverbounds.");
        }
		
		return new Coordinate(overages, Flags.VECTOR);
		
	}

    private int mnz(int[] dArr) {
        int mnz = Integer.MAX_VALUE;

        for (int x : dArr) {
            if (x == 0) {
                continue;
            } else if (Math.abs(x) < mnz) {
                mnz = Math.abs(x);
            }
        }

        return mnz;
    }

    /**
     * We call Math.abs(int) so many times here in algorithms
     * that it is easier to give a shorthand. Cloodgy but easy
     * to read (= Laziness.)
     */
    private int a(int i) {
        return Math.abs(i);
    }

    /**
     * Returns L1 norm.
     *
     */
    private int norm(int[] arr) {
        int sum = 0;

        for (int elem : arr) {
            sum += Math.abs(elem);
        }

        return sum;
    }

    private int getOverage(int u) {
        int du;
        if (u > radius) {
			du = u - radius;
		} else if (u < radius * -1) {
			du = u + radius;
		} else {
			du = 0;
		}

        return du;
    }

    @Override
	public int[] getDimensions() {
		int d = (2 * radius) + 1;
		
		return new int[] {d, d, d};
	}
	
	/*@Override
	public Coordinate[] getLimits() {
		// Since we only use a triangular lattice for hexagon, we can
		// hard code the limits.
		Coordinate d0 = new Coordinate(-1 * radius, -1 * radius, -1 * radius, Flags.VECTOR);
		Coordinate d1 = new Coordinate(radius, radius, radius, Flags.VECTOR);

		return new Coordinate[] {d0, d1};
	}*/
}
