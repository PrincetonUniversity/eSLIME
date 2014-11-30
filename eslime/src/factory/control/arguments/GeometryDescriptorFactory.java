/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;

import control.arguments.GeometryDescriptor;
import factory.geometry.lattice.LatticeFactory;
import factory.geometry.shape.ShapeFactory;
import geometry.lattice.Lattice;
import geometry.shape.Shape;
import org.dom4j.Element;

/**
 * Created by dbborens on 11/27/14.
 */
public abstract class GeometryDescriptorFactory {

    public static GeometryDescriptor instantiate(Element root) {
        Lattice lattice = makeLattice(root);
        Shape shape = makeShape(root, lattice);
        GeometryDescriptor geometryDescriptor = new GeometryDescriptor(lattice, shape);
        return geometryDescriptor;
    }

    private static Shape makeShape(Element root, Lattice lattice) {
        Element shapeElem = root.element("shape");
        Shape shape = ShapeFactory.instantiate(shapeElem, lattice);
        return shape;
    }

    private static Lattice makeLattice(Element root) {
        Element latticeElem = root.element("lattice");
        Lattice lattice = LatticeFactory.instantiate(latticeElem);
        return lattice;
    }
}
