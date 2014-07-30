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
