/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.factory;

import geometry.Geometry;
import geometry.boundaries.*;
import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Cuboid;
import geometry.shape.Hexagon;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import org.dom4j.Element;

/**
 * @author dbborens
 */
public class GeometryFactory {

    private Lattice lattice;
    private Shape shape;

    /**
     * @param root "geometry" tag, specifying project-level geometry information.
     */
    public GeometryFactory(Element root) {
        lattice = makeLattice(root);
        shape = makeShape(root, lattice);
    }

    /**
     * Default constructor included for testing
     */
    public GeometryFactory() {
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
        } else if (className.equalsIgnoreCase("XHardYArena")) {
            return new XHardYArena(shape, lattice);
        } else if (className.equalsIgnoreCase("XHardYPeriodic")) {
                return new XHardYPeriodic(shape, lattice);
        } else if (className.equalsIgnoreCase("PlaneRingHard")) {
            return new PlaneRingHard(shape, lattice);
        } else if (className.equalsIgnoreCase("PlaneRingReflecting")) {
            return new PlaneRingReflecting(shape, lattice);
        } else if (className.equalsIgnoreCase("Absorbing")) {
            return new Absorbing(shape, lattice);
        } else if (className.equalsIgnoreCase("periodic")) {
            return new Periodic(shape, lattice);
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
