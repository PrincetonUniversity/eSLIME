/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.boundaries;

import geometry.boundaries.*;
import geometry.lattice.Lattice;
import geometry.shape.Shape;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/31/14.
 */
public abstract class BoundaryFactory {
    public static Boundary instantiate(Element root, Lattice lattice, Shape shape) {

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
            return new HaltBoundary(shape, lattice);
        } else {
            String msg = "Unrecognized boundary class '" +
                    className + "'.";
            throw new IllegalArgumentException(msg);
        }
    }
}
