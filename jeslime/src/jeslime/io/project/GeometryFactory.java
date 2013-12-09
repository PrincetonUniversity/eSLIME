package jeslime.io.project;

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
public abstract class GeometryFactory {
	
	public static Geometry make(Element root) {
		Lattice lattice = makeLattice(root);
		Shape shape = makeShape(root, lattice);
		Boundary boundary = makeBoundary(root, lattice, shape);
		Geometry geom = new Geometry(lattice, shape, boundary);
		
		return geom;
	}

	private static Boundary makeBoundary(Element root, Lattice lattice,
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

	private static Lattice makeLattice(Element root) {
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

	private static Shape makeShape(Element root, Lattice lattice) {
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
