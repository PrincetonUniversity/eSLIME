/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.boundaries;

import control.arguments.GeometryDescriptor;
import geometry.boundaries.*;
import geometry.lattice.Lattice;
import geometry.shape.Shape;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/31/14.
 */
public abstract class BoundaryFactory {
    public static Boundary instantiate(Element root, GeometryDescriptor geometryDescriptor) {

        Shape shape = geometryDescriptor.getShape();
        Lattice lattice = geometryDescriptor.getLattice();

        String className = root.element("class").getTextTrim();

        if (className.equalsIgnoreCase("Arena")) {
            return new Arena(shape, lattice);
        } else if (className.equalsIgnoreCase("PlaneRingHard")) {
            return new PlaneRingHard(shape, lattice);
        } else if (className.equalsIgnoreCase("PlaneRingReflecting")) {
            return new PlaneRingReflecting(shape, lattice);
        } else if (className.equalsIgnoreCase("Absorbing")) {
            return new Absorbing(shape, lattice);
        } else if (className.equalsIgnoreCase("periodic")) {
            return new Periodic(shape, lattice);
        } else if (className.equalsIgnoreCase("halt")) {
            return new HaltArena(shape, lattice);
        } else if (className.equalsIgnoreCase("tetris")) {
            return new TetrisBoundary(shape, lattice);
        } else {
            String msg = "Unrecognized boundary class '" +
                    className + "'.";
            throw new IllegalArgumentException(msg);
        }
    }
}
