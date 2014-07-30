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
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.shape.Cuboid;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/7/14.
 */
public class WrapHelper3D extends WrapHelper {

    private final int width, height, depth;

    public WrapHelper3D(Shape shape, Lattice lattice) {
        super(shape, lattice);

        if (shape.getClass() != Cuboid.class) {
            throw new UnsupportedOperationException("WrapHelper3D only supports Cuboid arenas.");
        }

        Cuboid cuboid = (Cuboid) shape;

        width = cuboid.getDimensions()[0];
        height = cuboid.getDimensions()[1];
        depth = cuboid.getDimensions()[2];
    }

    public Coordinate wrapAll(Coordinate toWrap) {
        checkValid(toWrap);
        Coordinate xWrapped = xWrap(toWrap);
        Coordinate yWrapped = yWrap(xWrapped);
        Coordinate wrapped = zWrap(yWrapped);
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
                ret = new Coordinate(over - 1, ret.y(), ret.z(), ret.flags());

            } else {
                // over == -1 --> wrap to xMax (which is width - 1)
                ret = new Coordinate(width + over, ret.y(), ret.z(), ret.flags());
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
                ret = new Coordinate(ret.x(), over - 1, ret.z(), ret.flags());

            } else {
                // over == -1 --> wrap to xMax (which is width - 1)
                ret = new Coordinate(ret.x(), height + over, ret.z(), ret.flags());
            }

            over = shape.getOverbounds(ret).y();
        }

        // Return.
        return ret;
    }


    public Coordinate zWrap(Coordinate toWrap) {
        checkValid(toWrap);
        Coordinate ret = toWrap;

        // Wrap z.
        int over = shape.getOverbounds(ret).z();

        while (over != 0) {
            if (over > 0) {
                // over == 1 --> wrap to 0
                ret = new Coordinate(ret.x(), ret.y(), over - 1, ret.flags());

            } else {
                // over == -1 --> wrap to xMax (which is width - 1)
                ret = new Coordinate(ret.x(), ret.y(), depth + over, ret.flags());
            }

            over = shape.getOverbounds(ret).z();
        }

        // Return.
        return ret;
    }

    @Override
    protected void checkValid(Coordinate toWrap) {
        if (toWrap.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("WrapHelper3D requires a 3D coordinate.");
        }
    }
}
