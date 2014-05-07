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
import geometry.lattice.TriangularLattice;
import geometry.shape.Cuboid;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class Periodic extends Boundary {
    private WrapHelper helper;
    public Periodic(Shape shape, Lattice lattice) {
        super(shape, lattice);
        if (lattice.getDimensionality() == 2) {
            helper = new WrapHelper2D(shape, lattice);
        } else {
            helper = new WrapHelper3D(shape, lattice);
        }
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        if (lattice instanceof TriangularLattice) {
            throw new UnsupportedOperationException("Periodic boundary " +
                    "condition not yet supported on triangular lattice.");
        }

        if (!((shape instanceof Cuboid) || (shape instanceof Rectangle))) {
            throw new IllegalArgumentException("Full periodic boundary " +
                    "condition requires either a rectangle or cuboid arena" +
                    " shape.");
        }
    }

    @Override
    public Coordinate apply(Coordinate c) {
        return helper.wrapAll(c);
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new Periodic(scaledShape, clonedLattice);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Periodic periodic = (Periodic) o;

        if (helper != null ? !helper.equals(periodic.helper) : periodic.helper != null)
            return false;

        return true;
    }
}
