/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import geometry.boundaries.helpers.PlaneRingHelper;
import geometry.lattice.Lattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * One axis is a hard boundary, and the other is periodic.
 *
 * @author David Bruce Borenstein
 * @untested
 */
public class PlaneRingHard extends Boundary {

    private PlaneRingHelper helper;

    public PlaneRingHard(Shape shape, Lattice lattice) {
        super(shape, lattice);
        helper = new PlaneRingHelper(lattice, shape.getDimensions());
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        // If overbounds in Y direction, return undefined.
        if (ob.y() != 0) {
            return null;

            // If overbounds in X direction, return wrapped. Make sure to
            // adjust.
        } else if (ob.x() != 0) {
            return helper.wrap(c);

            // Otherwise, don't do anything.
        } else {
            return c;
        }
    }


    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        // PlaneRing is compatible only with Rectangle shapes.
        if (!(shape instanceof Rectangle)) {
            throw new IllegalArgumentException("PlaneRingHard boundary requires a Rectangle shape.");
        }
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new PlaneRingHard(scaledShape, clonedLattice);
    }
}
