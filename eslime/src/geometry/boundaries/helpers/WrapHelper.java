/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries.helpers;

import control.identifiers.Coordinate;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public abstract class WrapHelper {
    protected Lattice lattice;
    protected Shape shape;

    public WrapHelper(Shape shape, Lattice lattice) {
        this.lattice = lattice;
        this.shape = shape;
    }

    public abstract Coordinate wrapAll(Coordinate toWrap);

    public abstract Coordinate xWrap(Coordinate toWrap);

    public abstract Coordinate yWrap(Coordinate toWrap);

    public abstract Coordinate zWrap(Coordinate toWrap);

    protected abstract void checkValid(Coordinate toWrap);

    @Override
    public boolean equals(Object o) {
        return (o.getClass() == this.getClass());
    }
}
