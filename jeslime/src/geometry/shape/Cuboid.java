package geometry.shape;

import java.util.ArrayList;
import java.util.HashSet;

import org.dom4j.Element;

import geometry.lattice.Lattice;
import structural.Flags;
import structural.identifiers.Coordinate;

public class Cuboid extends Shape {

	private int height, width, depth;
	
	public Cuboid(Lattice lattice, int height, int width, int depth) {
		super(lattice);
		this.height = height;
		this.width = width;
		this.depth = depth;
		
		init();
	}

	public Cuboid(Lattice lattice, Element descriptor) {
		super(lattice);
		
		height = Integer.valueOf(descriptor.element("height").getTextTrim());
		width = Integer.valueOf(descriptor.element("width").getTextTrim());
		width = Integer.valueOf(descriptor.element("depth").getTextTrim());

		init();
	}
	@Override
	protected void verify(Lattice lattice) {
		if (lattice.getDimensionality() != 3) {
			throw new IllegalArgumentException("Cuboid lattice shape requires a 3D lattice connectivity.");
		}
	}

	@Override
	public Coordinate getCenter() {
		
		// 0-based coordinates
		int x = (width - 1)/ 2;
		int y = (height - 1)/ 2;
		int z = (depth - 1) / 2;
		Coordinate center = new Coordinate(x, y, z, 0);
		
		return center;
	}

	@Override
	public Coordinate[] getBoundaries() {
		HashSet<Coordinate> coords = new HashSet<Coordinate> (height * width * depth);
		
		// You could fix the bounds here so that no coordinate gets
		// checked twice and you don't need to convert from a hash
		// set to an array. But, "done is better than perfect." These
		// values can be precomputed and stored anyway.
		
		// Front and back
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				include(coords, new Coordinate(x, y, 0, 0));
				include(coords, new Coordinate(x, y, depth-1, 0));
			}
		}

		// Left and right
		for (int z = 0; z < depth; z++) {
			for (int y = 0; y < height; y++) {
				include(coords, new Coordinate(0, y, z, 0));
				include(coords, new Coordinate(width-1, y, z, 0));
			}
		}
		
		// Top and bottom
		for (int z = 0; z < depth; z++) {
			for (int x = 0; x < width; x++) {
				include(coords, new Coordinate(x, 0, z, 0));
				include(coords, new Coordinate(x, height-1, z, 0));
			}
		}
		
		return coords.toArray(new Coordinate[0]);
	}

	@Override
	protected Coordinate[] calcSites() {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate> (height*width*depth);

		for (int z = 0; z < depth; z++) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					include(coords, new Coordinate(x, y, z, 0));
				}
			}
		}
		
		return coords.toArray(new Coordinate[0]);
	}
	
	@Override
	public Coordinate getOverbounds(Coordinate coord) {
		// Get orthogonal distance from (0, 0, 0) to this point.
		Coordinate origin = new Coordinate(0, 0, 0, 0);
		
		Coordinate d = lattice.getOrthoDisplacement(origin, coord);
		
		int dx, dy, dz;
		
		// Handle x coordinate overbounds
		if (d.x() < 0) {
			dx = d.x();
		} else if (d.x() >= width) {
			dx = d.x() - width + 1;
		} else {
			// If within bounds, overbounds amount is zero.
			dx = 0;
		}
		
		// Handle y coordinate overbounds
		if (d.y() < 0) {
			dy = d.y();
		} else if (d.y() >= height) {
			dy = d.y() - height + 1;
		} else {
			// If within bounds, overbounds amount is zero.
			dy = 0;
		}
		
		// Handle x coordinate overbounds
		if (d.z() < 0) {
			dz = d.z();
		} else if (d.z() >= depth) {
			dz = d.z() - depth + 1;
		} else {
			// If within bounds, overbounds amount is zero.
			dz = 0;
		}
		
		return new Coordinate(dx, dy, dz, Flags.VECTOR);
	}

	@Override
	public int[] getDimensions() {
		return new int[] {width, height, depth};
	}

    @Override
    public boolean equals(Object obj) {
        // Is it a Cuboid?
        if (!(obj instanceof  Cuboid)) {
            return false;
        }

        Cuboid other = (Cuboid) obj;

        // Does it have the same dimensions?
        if (other.width != this.width) {
            return false;
        }

        if (other.height != this.height) {
            return false;
        }

        if (other.depth != this.depth) {
            return false;
        }

        // If these things are OK, return true
        return true;
    }
}
