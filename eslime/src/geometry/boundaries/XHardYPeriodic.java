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
import geometry.boundaries.helpers.WrapHelper;
import geometry.boundaries.helpers.WrapHelper2D;
import geometry.boundaries.helpers.WrapHelper3D;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Cuboid;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class XHardYPeriodic extends Boundary {
    protected WrapHelper helper;
    public XHardYPeriodic(Shape shape, Lattice lattice) {
        super(shape, lattice);
            helper = new WrapHelper2D(shape, lattice);
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        if (lattice.getClass() != RectangularLattice.class) {
            throw new IllegalArgumentException("X-hard/Y-periodic BC requires a 2D rectangular lattice.");
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
            ret = helper.yWrap(c);
        } else {
            ret = c;
        }

        return ret;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new XHardYPeriodic(scaledShape, clonedLattice);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XHardYPeriodic periodic = (XHardYPeriodic) o;

        if (helper != null ? !helper.equals(periodic.helper) : periodic.helper != null)
            return false;

        return true;
    }
}
