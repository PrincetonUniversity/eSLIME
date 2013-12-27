package geometry;

import structural.Flags;
import structural.identifiers.Coordinate;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 12/24/13.
 */
public class LinearMockGeometry extends MockGeometry {
    // Used to emulate a boundary condition. Note that the mock BC only
    // is defined along one edge, so use with caution.
    private int width = -1;

    public LinearMockGeometry() {
        super();
        setConnectivity(1);
        setDimensionality(1);
    }

    @Override
    public Coordinate[] getNeighbors(Coordinate coord, int mode) {

        if (!coord.hasFlag(Flags.PLANAR))
            throw new IllegalStateException();

        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        int x = coord.x();
        int y = coord.y();

        consider(neighbors, x+1, y);
        consider(neighbors, x-1, y);

        return neighbors.toArray(new Coordinate[0]);
    }

    public Coordinate rel2abs(Coordinate coord, Coordinate displacement, int mode) {
        // Only works in x direction
        int x1 = coord.x() + displacement.x();
        if (width > 0 && x1 >= width) {
            return null;
        }

        int y1 = coord.y();
        int f1 = coord.flags();

        Coordinate c = new Coordinate(x1, y1, f1);

        return c;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
