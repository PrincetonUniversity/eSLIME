package io.project;

import geometry.*;
import geometry.boundaries.*;
import geometry.lattice.*;
import geometry.shape.*;

import org.dom4j.Element;

/**
 * 
 * @author dbborens
 * @untested
 * 
 */
public class GeometryManager {

    private Lattice lattice;
    private Shape shape;

    /**
     * @param root "geometry" tag, specifying project-level geometry information.
     */
    public GeometryManager(Element root) {
        lattice = makeLattice(root);
        shape = makeShape(root, lattice);
    }
	public Geometry make(Element layerRoot) {
		Boundary boundary = makeBoundary(layerRoot, lattice, shape);
		Geometry geom = new Geometry(lattice, shape, boundary);
		
		return geom;
	}

	private Boundary makeBoundary(Element root, Lattice lattice,
			Shape shape) {

		Element boundaryElem = root.element("boundary");
		String className = boundaryElem.element("class").getTextTrim();

		if (className.equalsIgnoreCase("Arena")) {
			return new Arena(shape, lattice);
		} else if (className.equalsIgnoreCase("PlaneRingHard")) {
			return new PlaneRingHard(shape, lattice);
		} else if (className.equalsIgnoreCase("PlaneRingReflecting")) {
			return new PlaneRingReflecting(shape, lattice);
		} else {
			String msg = "Unrecognized boundary class '" +
					className + "'.";
			throw new IllegalArgumentException(msg);
		}
	}

	private Lattice makeLattice(Element root) {
		Element latticeElem = root.element("lattice");
		String className = latticeElem.element("class").getTextTrim();

		if (className.equalsIgnoreCase("Rectangular")) {
			return new RectangularLattice();
		} else if (className.equalsIgnoreCase("Triangular")) {
			return new TriangularLattice();
		} else if (className.equalsIgnoreCase("Cubic")) {
			return new CubicLattice();
		} else {
			String msg = "Unrecognized lattice class '" +
					className + "'.";
			throw new IllegalArgumentException(msg);
		}
	}

	private Shape makeShape(Element root, Lattice lattice) {
		Element shapeElem = root.element("shape");
		String className = shapeElem.element("class").getTextTrim();
		
		if (className.equalsIgnoreCase("Rectangle")) {
			return new Rectangle(lattice, shapeElem);
		} else if (className.equalsIgnoreCase("Hexagon")) {
			return new Hexagon(lattice, shapeElem);
		} else if (className.equalsIgnoreCase("Cuboid")) {
			return new Cuboid(lattice, shapeElem);
		} else {
			String msg = "Unrecognized shape class '" +
					className + "'.";
			throw new IllegalArgumentException(msg);
		}

	}
	
}
