/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import geometry.boundaries.helpers.WrapHelper;
import geometry.boundaries.helpers.WrapHelper1D;
import geometry.boundaries.helpers.WrapHelper2D;
import geometry.boundaries.helpers.WrapHelper3D;
import geometry.lattice.Lattice;
import geometry.shape.Cuboid;
import geometry.shape.Line;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class Periodic extends Boundary {
    protected WrapHelper helper;
    public Periodic(Shape shape, Lattice lattice) {
        super(shape, lattice);
        if (lattice.getDimensionality() == 1) {
            helper = new WrapHelper1D(shape, lattice);
        } else if (lattice.getDimensionality() == 2) {
            helper = new WrapHelper2D(shape, lattice);
        } else {
            helper = new WrapHelper3D(shape, lattice);
        }
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        if (!((shape instanceof Cuboid) || (shape instanceof Rectangle) ||
                (shape instanceof Line))) {
            throw new IllegalArgumentException("Full periodic boundary " +
                    "condition requires a line, rectangle or cuboid arena" +
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
