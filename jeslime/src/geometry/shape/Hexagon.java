package geometry.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		Coordinate raw = new Coordinate(radius, radius, 0);
		Coordinate adjusted = lattice.adjust(raw);
		
		return adjusted;
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
	public Coordinate getOverbounds(Coordinate coord) {
		// Get natural-basis displacement from center
		Coordinate origin = getCenter();
		Coordinate d = lattice.getDisplacement(origin, coord);
		
		// Remember that diameter of Hexagon geometry is 2r + 1,
		// so out of bounds is r > R (strictly greater)
		
		int du, dv, dw;
		
		int u = d.x();
		int v = d.y();
		int w = d.z();
		
		//System.out.println("A: " + u + "\t" + v + "\t" + w + " | " + radius);
		// Handle "u" component (southeast)
		if (u > radius) {
			du = u - radius;
		} else if (u < radius * -1) {
			du = u + radius;
		} else {
			du = 0;
		}
		
		// Handle "v" component (northeast)
		if (v > radius) {
			dv = v - radius;
		} else if (v < radius * -1) {
			dv = v + radius;
		} else {
			dv = 0;
		}
		
		// Handle "w" component (north)
		if (w > radius) {
			dw = w - radius;
		} else if (w < radius * -1) {
			dw = w + radius;
		} else {
			dw = 0;
		}
		
		return new Coordinate(du, dv, dw, Flags.VECTOR);
		
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
