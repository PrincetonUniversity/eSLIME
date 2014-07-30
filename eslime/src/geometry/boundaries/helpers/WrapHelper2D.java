/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries.helpers;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class WrapHelper2D extends WrapHelper {

    private final int width, height;

    public WrapHelper2D(Shape shape, Lattice lattice) {
        super(shape, lattice);

        if (shape.getClass() != Rectangle.class) {
            throw new UnsupportedOperationException("WrapHelper2D only supports Rectangle arenas.");
        }

        Rectangle rect = (Rectangle) shape;

        width = rect.getDimensions()[0];
        height = rect.getDimensions()[1];
    }

    public Coordinate wrapAll(Coordinate toWrap) {
        checkValid(toWrap);
        Coordinate xWrapped = xWrap(toWrap);
        Coordinate wrapped = yWrap(xWrapped);
        return wrapped;
    }

    public Coordinate xWrap(Coordinate toWrap) {
        checkValid(toWrap);
        // Remove any x-adjustment from y.
        Coordinate ret = lattice.invAdjust(toWrap);

        // Wrap x.
        int over = shape.getOverbounds(ret).x();

        while (over != 0) {
            if (over > 0) {
                // over == 1 --> wrap to 0
                ret = new Coordinate(over - 1, ret.y(), ret.flags());

            } else {
                // over == -1 --> wrap to xMax (which is width - 1)
                ret = new Coordinate(width + over, ret.y(), ret.flags());
            }
            over = shape.getOverbounds(ret).x();
        }

        // Apply x-adjustment to y.
        ret = lattice.adjust(ret);

        // Return new coordinate.
        return ret;
    }

    public Coordinate yWrap(Coordinate toWrap) {
        checkValid(toWrap);
        Coordinate ret = toWrap;

        // Wrap y.
        int over = shape.getOverbounds(ret).y();

        while (over != 0) {
            if (over > 0) {
                // over == 1 --> wrap to 0
                ret = new Coordinate(ret.x(), over - 1, ret.flags());

            } else {
                // over == -1 --> wrap to xMax (which is width - 1)
                ret = new Coordinate(ret.x(), height + over, ret.flags());
            }

            over = shape.getOverbounds(ret).y();
        }

        // Return.
        return ret;
    }


    public Coordinate zWrap(Coordinate toWrap) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void checkValid(Coordinate toWrap) {
        if (!toWrap.hasFlag(Flags.PLANAR)) {
            throw new IllegalStateException("WrapHelper2D requires a 2D coordinate.");
        }
    }
}
