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

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package geometry.lattice;

import control.identifiers.Coordinate;
import control.identifiers.Flags;

public class LinearLattice extends Lattice {

    @Override
    public int getConnectivity() {
        return 1;
    }

    @Override
    public int getDimensionality() {
        return 1;
    }

    @Override
    public Coordinate adjust(Coordinate toAdjust) {
        if (!toAdjust.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Linear lattice (unfortunately) " +
                    "expects planar coordinates. I'm working on it.");
        }

        // A rectangular lattice requires no offset adjustment to be consistent
        // with Cartesian coordinates.
        return toAdjust;
    }

    protected void defineBasis() {

        Coordinate north = new Coordinate(0, 1, 0);

        basis = new Coordinate[]{north};
    }

    @Override
    public Coordinate[] getAnnulus(Coordinate coord, int r) {
        // De-index coordinate.
        int x0 = coord.x();

        if (x0 != 0) {
            throw new IllegalStateException("Linear lattice expects strictly zero x  values.");
        }

        int y0 = coord.y();

        // r=0 case (a point)
        if (r == 0) {
            return new Coordinate[]{new Coordinate(x0, y0, 0)};
        }

        Coordinate northern = new Coordinate(x0, y0 + r, 0);
        Coordinate southern = new Coordinate(x0, y0 - r, 0);

        return new Coordinate[] {northern, southern};
    }

    @Override
    public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord) {
        if (!pCoord.hasFlag(Flags.PLANAR) || !qCoord.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expect planar coordinates in " +
                    "linear lattice. (I know, it doesn't make sense. I have not" +
                    "yet implemented 1D coordinates.)");
        }

        if (pCoord.x() != 0 || pCoord.z() != 0) {
            throw new IllegalStateException("Expect strictly 0 x coordinate for" +
                    " linear lattice.");
        }
        int dx = 0;
        int dy = qCoord.y() - pCoord.y();

        return new Coordinate(dx, dy, Flags.VECTOR);
    }

    @Override
    public Coordinate rel2abs(Coordinate coord, Coordinate displacement) {
        int x = coord.x();

        if (coord.x() != 0 || coord.z() != 0) {
            throw new IllegalStateException("Expect strictly 0 x and z " +
                    "coordinates for linear lattice.");
        }
        int y = coord.y();

        // Apply x component
        x += displacement.x();


        // Apply y component
        y += displacement.y();

        Coordinate target = new Coordinate(x, y, 0);

        return target;
    }

    @Override
    public Coordinate getOrthoDisplacement(Coordinate pCoord, Coordinate qCoord) {
        return getDisplacement(pCoord, qCoord);
    }

    @Override
    public Coordinate invAdjust(Coordinate toAdjust) {
        return toAdjust;
    }

    @Override
    public Lattice clone() {
        return new LinearLattice();
    }

    @Override
    public Coordinate getZeroVector() {
        return new Coordinate(0, 0, 0);
    }
}
