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

package geometry;

import structural.Flags;
import structural.identifiers.Coordinate;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 12/24/13.
 */
public class CubicMockGeometry extends MockGeometry {
    // Used to emulate a boundary condition. Note that the mock BC only
    // is defined along one edge, so use with caution.
    private int width = -1;

    public CubicMockGeometry() {
        super();
        setConnectivity(3);
        setDimensionality(3);
    }

    @Override
    public Coordinate[] getNeighbors(Coordinate coord, int mode) {
        if (coord.hasFlag(Flags.PLANAR))
            throw new IllegalStateException(coord.toString());

        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        int x = coord.x();
        int y = coord.y();
        int z = coord.z();

        consider(neighbors, x + 1, y, z);
        consider(neighbors, x - 1, y, z);
        consider(neighbors, x, y + 1, z);
        consider(neighbors, x, y - 1, z);
        consider(neighbors, x, y, z + 1);
        consider(neighbors, x, y, z - 1);

        return neighbors.toArray(new Coordinate[0]);
    }

    public Coordinate rel2abs(Coordinate coord, Coordinate displacement, int mode) {
        // Only works in x direction
        int x1 = coord.x() + displacement.x();

        if (width > 0 && x1 >= width) {
            return null;
        }
        int y1 = coord.y();
        int z1 = coord.z();

        int f1 = coord.flags();

        return new Coordinate(x1, y1, z1, f1);
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
