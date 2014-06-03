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

import java.util.HashSet;

public class CubicLattice extends Lattice {

    protected void defineBasis() {
        Coordinate i = new Coordinate(1, 0, 0, 0);
        Coordinate j = new Coordinate(0, 1, 0, 0);
        Coordinate k = new Coordinate(0, 0, 1, 0);

        basis = new Coordinate[]{i, j, k};
    }

    @Override
    public int getConnectivity() {
        return 3;
    }

    @Override
    public int getDimensionality() {
        return 3;
    }

    @Override
    public Coordinate adjust(Coordinate i) {
        if (i.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Cubic lattice is a 3D geometry.");
        }

        return i;
    }

    @Override
    public Coordinate[] getAnnulus(Coordinate coord, int r) {
        // De-index coordinate.
        int x0 = coord.x();
        int y0 = coord.y();
        int z0 = coord.z();

        // r=0 case (a point)
        if (r == 0) {
            return new Coordinate[]{new Coordinate(x0, y0, 0)};
        }

        // All other cases
        HashSet<Coordinate> ring = new HashSet<Coordinate>();

        for (int dx = 0; dx <= r; dx++) {
            for (int dy = 0; dy <= r - dx; dy++) {
                int dz = r - dx - dy;
                //System.out.println("r=" + r + " dx=" + dx + " dy=" + dy + " dz=" + dz);

                // TODO: This is redundant and may be inefficient
                ring.add(new Coordinate(x0 + dx, y0 + dy, z0 + dz, 0));
                ring.add(new Coordinate(x0 + dx, y0 + dy, z0 - dz, 0));
                ring.add(new Coordinate(x0 + dx, y0 - dy, z0 + dz, 0));
                ring.add(new Coordinate(x0 + dx, y0 - dy, z0 - dz, 0));
                ring.add(new Coordinate(x0 - dx, y0 + dy, z0 + dz, 0));
                ring.add(new Coordinate(x0 - dx, y0 + dy, z0 - dz, 0));
                ring.add(new Coordinate(x0 - dx, y0 - dy, z0 + dz, 0));
                ring.add(new Coordinate(x0 - dx, y0 - dy, z0 - dz, 0));

            }

        }

        return ring.toArray(new Coordinate[0]);
    }

    @Override
    public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord) {
        if (pCoord.hasFlag(Flags.PLANAR) || qCoord.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expect 3D coordinates in cubic lattice.");
        }

        int dx = qCoord.x() - pCoord.x();
        int dy = qCoord.y() - pCoord.y();
        int dz = qCoord.z() - pCoord.z();

        return new Coordinate(dx, dy, dz, Flags.VECTOR);
    }

    @Override
    public Coordinate rel2abs(Coordinate coord, Coordinate displacement) {
        if (displacement.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expected three arguments to cubic lattice rel2abs.");
        }

        int x = coord.x();
        int y = coord.y();
        int z = coord.z();

        // Apply x component
        x += displacement.x();

        // Apply y component
        y += displacement.y();

        // Apply z component
        z += displacement.z();

        Coordinate target = new Coordinate(x, y, z, 0);

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
        return new CubicLattice();
    }
}
