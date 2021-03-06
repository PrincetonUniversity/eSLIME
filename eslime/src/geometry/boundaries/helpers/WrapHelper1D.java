/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries.helpers;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.shape.Line;
import geometry.shape.Rectangle;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class WrapHelper1D extends WrapHelper {

    private final int length;

    public WrapHelper1D(Shape shape, Lattice lattice) {
        super(shape, lattice);

        if (shape.getClass() != Line.class) {
            throw new UnsupportedOperationException("WrapHelper1D only supports Line arenas.");
        }

        Line line = (Line) shape;

        length = line.getDimensions()[0];
    }

    public Coordinate wrapAll(Coordinate toWrap) {
        checkValid(toWrap);
        Coordinate wrapped = yWrap(toWrap);
        return wrapped;
    }

    public Coordinate xWrap(Coordinate toWrap) {
        throw new UnsupportedOperationException();
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
                ret = new Coordinate(ret.x(), length + over, ret.flags());
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
        if (toWrap.x() != 0 || toWrap.z() != 0) {
            throw new IllegalStateException("WrapHelper1D requires strictly 0 x and z values.");
        }
    }
}
