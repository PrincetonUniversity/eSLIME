/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.shape;

import geometry.lattice.Lattice;
import geometry.shape.*;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/31/14.
 */
public abstract class ShapeFactory {

    public static Shape instantiate(Element root, Lattice lattice) {

        String className = root.element("class").getTextTrim();

        if (className.equalsIgnoreCase("Line")) {
            return new Line(lattice, root);
        } if (className.equalsIgnoreCase("Rectangle")) {
            return new Rectangle(lattice, root);
        } else if (className.equalsIgnoreCase("Hexagon")) {
            return new Hexagon(lattice, root);
        } else if (className.equalsIgnoreCase("Cuboid")) {
            return new Cuboid(lattice, root);
        } else {
            String msg = "Unrecognized shape class '" +
                    className + "'.";
            throw new IllegalArgumentException(msg);
        }

    }
}
