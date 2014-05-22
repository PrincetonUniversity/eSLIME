/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.boundaries.helpers.WrapHelper;
import geometry.boundaries.helpers.WrapHelper2D;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class XHardYArena extends Boundary {
    public XHardYArena(Shape shape, Lattice lattice) {
        super(shape, lattice);
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        if (lattice.getClass() != RectangularLattice.class) {
            throw new IllegalArgumentException("X-hard/Y-arena BC requires a 2D rectangular lattice.");
        }
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        Coordinate ret;

        if (ob.x() != 0) {
            return null;
        }

        if (ob.y() != 0) {
            ret = c.addFlags(Flags.END_OF_WORLD | Flags.BOUNDARY_APPLIED);
        } else {
            ret = c;
        }

        return ret;
    }

    @Override
    public boolean isInfinite() {
        return true;
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new XHardYArena(scaledShape, clonedLattice);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }
}
