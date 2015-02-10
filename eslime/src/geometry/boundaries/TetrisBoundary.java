/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.boundaries.helpers.WrapHelper2D;
import geometry.lattice.Lattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Periodic in x, hard on southern y boundary, absorbing, on northern y
 * boundary, halt on absorption.
 * <p>
 * Created by dbborens on 2/9/15.
 */
public class TetrisBoundary extends Boundary implements HaltBoundary {

    private WrapHelper2D wrapper;

    public TetrisBoundary(Shape shape, Lattice lattice) {
        super(shape, lattice);
        wrapper = new WrapHelper2D(shape, lattice);
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        if (!(shape instanceof Rectangle)) {
            throw new IllegalArgumentException("Tetris boundary condition requires a Rectangle shape");
        }

        if (lattice.getDimensionality() != 2) {
            throw new IllegalArgumentException("Tetris boundary condition only well defined in 2D");
        }
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);
        Coordinate wrapped = wrapper.xWrap(c);

        // I have to get rid of these stupid flags. Where absolutely
        // necessary, they can be interfaces.
        if (!wrapped.equals(c)) {
            wrapped = wrapped.addFlags(Flags.BOUNDARY_APPLIED);
        }

        if (ob.y() < 0) {
            return null;
        } else if (ob.y() > 0) {
            return wrapped.addFlags(Flags.BOUNDARY_APPLIED | Flags.END_OF_WORLD);
        }

        return wrapped;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return null;
    }
}
