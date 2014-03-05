/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package io.project;

import geometry.*;
import geometry.boundaries.*;
import geometry.lattice.*;
import geometry.shape.*;

import org.dom4j.Element;

/**
 * 
 * @author dbborens
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

    /**
     * Default constructor included for testing
     */
    public GeometryManager() {}

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
        } else if (className.equalsIgnoreCase("Absorbing")) {
            return new Absorbing(shape, lattice);
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
