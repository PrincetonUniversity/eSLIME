/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * A boundary condition that returns null for any overbound
 * position. Since Geometry removes null values, the result
 * is that cells adjacent to the boundary have fewer neighbors
 * than cells at the interior.
 * <p/>
 * Created by David B Borenstein on 12/22/13.
 */
public class Absorbing extends Boundary {

    public Absorbing(Shape shape, Lattice lattice) {
        super(shape, lattice);
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        Coordinate ret;
        // Is any component overbound?
        if (ob.x() != 0 || ob.y() != 0 || ob.z() != 0) {
            // If yes return null
            ret = null;

        } else {
            // Otherwise, just return the coordinate.
            ret = c;
        }

        return ret;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        // Absorbing is compatible with all lattice geometries and
        // shapes, so no verification is needed.
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new Absorbing(scaledShape, clonedLattice);
    }
}
