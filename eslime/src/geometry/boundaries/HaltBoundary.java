/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * The HaltBoundary works just like an arena. The class itself is used to flag
 * the fact that reaching the boundary should result in halting the simulation.
 *
 * Created by dbborens on 7/30/14.
 */
public class HaltBoundary extends Arena {
    public HaltBoundary(Shape shape, Lattice lattice) {
        super(shape, lattice);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new HaltBoundary(scaledShape, clonedLattice);
    }
}
