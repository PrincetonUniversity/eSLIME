/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry;

import control.identifiers.Coordinate;
import control.identifiers.Flags;

import java.util.ArrayList;

public class TriangularMockGeometry extends MockGeometry {
    // Used to emulate a boundary condition. Note that the mock BC only
    // is defined along one edge, so use with caution.
    private int radius;

    public TriangularMockGeometry() {
        super();
        setConnectivity(3);
        setDimensionality(2);
    }

    @Override
    public Coordinate[] getNeighbors(Coordinate coord, int mode) {
        if (!coord.hasFlag(Flags.PLANAR))
            throw new IllegalStateException();

        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        int x = coord.x();
        int y = coord.y();

        consider(neighbors, x, y + 1);
        consider(neighbors, x + 1, y + 1);
        consider(neighbors, x + 1, y);
        consider(neighbors, x, y - 1);
        consider(neighbors, x - 1, y - 1);
        consider(neighbors, x - 1, y);

        return neighbors.toArray(new Coordinate[0]);
    }

    public Coordinate rel2abs(Coordinate coord, Coordinate displacement, int mode) {
        // Only works in x direction
        int x1 = coord.x() + displacement.x();
        if (radius > 0 && x1 >= radius) {
            return null;
        }
        int y1 = coord.y();
        int f1 = coord.flags();

        return new Coordinate(x1, y1, f1);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
