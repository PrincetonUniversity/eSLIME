/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * All boundaries are treated as infinite. However, going beyond
 * a specified limit results in the END_OF_WORLD flag being set.
 * This flag can then be handled within a specific process. In
 * practice, this amounts to a kind of absorbing boundary.
 *
 * @author David Bruce Borenstein
 * @test ArenaTest.java
 */
public class Arena extends Boundary {

    public Arena(Shape shape, Lattice lattice) {
        super(shape, lattice);
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        Coordinate ret;
        // Is any component overbound?
        if (ob.x() != 0 || ob.y() != 0 || ob.z() != 0) {
            // If yes, set the appropriate flags
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
    protected void verify(Shape shape, Lattice lattice) {
        // Arena is compatible with all lattice geometries and
        // shapes, so no verification is needed.
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new Arena(scaledShape, clonedLattice);
    }
}
