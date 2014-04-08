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

import java.util.ArrayList;

public class TriangularLattice extends Lattice {

    protected void defineBasis() {
        Coordinate southeast = new Coordinate(1, 0, 0);
        Coordinate northeast = new Coordinate(1, 1, 0);
        Coordinate north = new Coordinate(0, 1, 0);

        basis = new Coordinate[]{southeast, northeast, north};
    }

    @Override
    public int getConnectivity() {
        return 3;
    }

    @Override
    public int getDimensionality() {
        return 2;
    }

    @Override
    public Coordinate adjust(Coordinate i) {
        if (!i.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Triangular lattice is a planar geometry.");
        }

        int yAdj;
        int x = i.x();
        if (x >= 0) {
            yAdj = (x / 2);
        } else {
            yAdj = ((x - 1) / 2);
        }

        Coordinate o = new Coordinate(i.x(), i.y() + yAdj, i.flags());

        return o;
    }

    @Override
    public Coordinate invAdjust(Coordinate i) {
        if (!i.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Triangular lattice is a planar geometry.");
        }

        int yAdj;
        int x = i.x();
        if (x >= 0) {
            yAdj = (x / 2);
        } else {
            yAdj = ((x - 1) / 2);
        }
        Coordinate o = new Coordinate(i.x(), i.y() - yAdj, i.flags());

        return o;
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
        ArrayList<Coordinate> ring = new ArrayList<Coordinate>(n);

        for (int k = 1; k <= r; k++) {
            // Right side
            Coordinate candidate = new Coordinate(x0 + r, y0 + k - 1, 0);
            ring.add(candidate);

            // Lower right side
            candidate = new Coordinate(x0 + r - k, y0 - k, 0);
            ring.add(candidate);

            // Lower left side
            candidate = new Coordinate(x0 - k, y0 - r, 0);
            ring.add(candidate);

            // Left side
            candidate = new Coordinate(x0 - r, y0 - r + k, 0);
            ring.add(candidate);

            // Upper left side
            candidate = new Coordinate(x0 - r + k, y0 + k, 0);
            ring.add(candidate);

            // Upper right side
            candidate = new Coordinate(x0 + k, y0 + r, 0);
            ring.add(candidate);
        }

        return (ring.toArray(new Coordinate[0]));

    }

    /* Get (naive) size of an annulus of the specified L1 radius from a
     * point, i.e., size assuming no out-of-bounds points.
     */
    private int getAnnulusSize(int radius) {
        if (radius == 0) {
            return (1);
        }

        return (6 * radius);
    }

    @Override
    public Coordinate getDisplacement(Coordinate p, Coordinate q) {
        // Extract p coordinate
        int xp = p.x();
        int yp = p.y();

        // Extract q coordinate
        int xq = q.x();
        int yq = q.y();

        int dx = xq - xp;
        int dy = yq - yp;

        // A triangular lattice is formed by a non-orthogonal basis:
        //    u = [1, 0] --> southeast
        //    v = [1, 1] --> northeast
        //    w = [0, 1] --> north

        // Step 1: get v component.
        int dv = 0;
        if (dx < 0 && dy < 0) {
            dv = Math.max(dx, dy);
        }

        if (dx > 0 && dy > 0) {
            dv = Math.min(dx, dy);
        }

        // Step 2: subtracting du from dx and dy, we get dw and du respectively.
        int du = dx - dv;
        int dw = dy - dv;

        // Populate the vector in the new basis -- done!
        return new Coordinate(du, dv, dw, Flags.VECTOR);
    }

    @Override
    public Coordinate getOrthoDisplacement(Coordinate p, Coordinate q) {
        // The 'u' and 'w' components are linearly independent. (Not
        // exactly 'orthogonal,' but oh well.)

        // Extract p coordinate
        int xp = p.x();
        int yp = p.y();

        // Extract q coordinate
        int xq = q.x();
        int yq = q.y();

        int du = xq - xp;
        int dv = 0;
        int dw = yq - yp;

        return new Coordinate(du, dv, dw, Flags.VECTOR);
    }


    @Override
    public Coordinate rel2abs(Coordinate coord, Coordinate displacement) {
        if (displacement.hasFlag(Flags.PLANAR)) {
            throw new IllegalArgumentException("Expected three arguments to HexArena::rel2abs(...)");
        }

        int x = coord.x();
        int y = coord.y();

        // Apply u component
        x += displacement.x();

        // Apply v component
        x += displacement.y();
        y += displacement.y();

        // Apply w component
        y += displacement.z();

        Coordinate target = new Coordinate(x, y, 0);

        return target;
    }

    @Override
    public Lattice clone() {
        return new TriangularLattice();
    }
}
