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

import structural.identifiers.Coordinate;
import structural.identifiers.Flags;

import java.util.HashSet;

public class RectangularLattice extends Lattice {

    @Override
    public int getConnectivity() {
        return 2;
    }

    @Override
    public int getDimensionality() {
        return 2;
    }

    @Override
    public Coordinate adjust(Coordinate toAdjust) {
        if (!toAdjust.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Rectangular lattice is a planar geometry.");
        }

        // A rectangular lattice requires no offset adjustment to be consistent
        // with Cartesian coordinates.
        return toAdjust;
    }

    protected void defineBasis() {

        Coordinate east = new Coordinate(1, 0, 0);
        Coordinate north = new Coordinate(0, 1, 0);

        basis = new Coordinate[]{east, north};
    }

    @Override
    public Coordinate[] getAnnulus(Coordinate coord, int r) {
        // De-index coordinate.
        int x0 = coord.x();
        int y0 = coord.y();

        int n = getAnnulusSize(r);

        // r=0 case (a point)
        if (r == 0) {
            return new Coordinate[]{new Coordinate(x0, y0, 0)};
        }

        // All other cases
        HashSet<Coordinate> ring = new HashSet<Coordinate>(n);

        for (int dx = 0; dx <= r; dx++) {
            int dy = r - dx;

            // TODO: This is redundant and may be inefficient
            ring.add(new Coordinate(x0 + dx, y0 + dy, 0));
            ring.add(new Coordinate(x0 - dx, y0 + dy, 0));
            ring.add(new Coordinate(x0 + dx, y0 - dy, 0));
            ring.add(new Coordinate(x0 - dx, y0 - dy, 0));
        }

        return ring.toArray(new Coordinate[0]);
    }

    /* Get (naive) size of an annulus of the specified L1 radius from a
     * point, i.e., size assuming no out-of-bounds points.
     */
    private int getAnnulusSize(int radius) {
        if (radius == 0) {
            return (1);
        }

        return (4 * radius);
    }

    @Override
    public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord) {
        if (!pCoord.hasFlag(Flags.PLANAR) || !qCoord.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expect planar coordinates in rectangular lattice.");
        }

        int dx = qCoord.x() - pCoord.x();
        int dy = qCoord.y() - pCoord.y();

        return new Coordinate(dx, dy, Flags.VECTOR);
    }

    @Override
    public Coordinate rel2abs(Coordinate coord, Coordinate displacement) {
        if (!displacement.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expected two arguments to rectangular lattice rel2abs.");
        }

        int x = coord.x();
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
        return new RectangularLattice();
    }
}