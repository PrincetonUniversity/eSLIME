/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry;

import factory.geometry.boundaries.BoundaryFactory;
import factory.geometry.lattice.LatticeFactory;
import factory.geometry.shape.ShapeFactory;
import geometry.Geometry;
import geometry.boundaries.*;
import geometry.lattice.*;
import geometry.shape.*;
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
        return BoundaryFactory.instantiate(boundaryElem, lattice, shape);
    }

    private Lattice makeLattice(Element root) {
        Element latticeElement = root.element("lattice");
        return LatticeFactory.instantiate(latticeElement);
    }

    private Shape makeShape(Element root, Lattice lattice) {
        Element shapeElem = root.element("shape");
        return ShapeFactory.instantiate(shapeElem, lattice);
    }

}
