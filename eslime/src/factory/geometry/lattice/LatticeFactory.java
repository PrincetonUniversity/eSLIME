/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.lattice;

import geometry.lattice.*;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/31/14.
 */
public abstract class LatticeFactory {

    public static Lattice instantiate(Element root) {
        String className = root.element("class").getTextTrim();

        if (className.equalsIgnoreCase("Linear")) {
            return new LinearLattice();
        } else if (className.equalsIgnoreCase("Rectangular")) {
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
}
