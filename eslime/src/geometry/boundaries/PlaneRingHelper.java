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

package geometry.boundaries;

import geometry.lattice.Lattice;
import structural.identifiers.Coordinate;
import structural.identifiers.Flags;

/**
 * Wraps coordinates in the X direction, honoring any features of the
 * lattice.
 * <p/>
 * TODO: Generalize this cloodgy class to make it more reusable.
 *
 * @author dbborens
 */
public class PlaneRingHelper {

    private Lattice lattice;
    private int width;
    private int height;

    public PlaneRingHelper(Lattice lattice, int[] dims) {
        this.lattice = lattice;
        this.width = dims[0];
        this.height = dims[1];
    }

    /**
     * Wrap a coordinate in the x direction. This is really only public
     * for testing purposes.
     *
     * @param c
     * @return
     */
    public Coordinate wrap(Coordinate c) {
        // We have to do a lot of work because the coordinate could be
        // in any basis. That might not be true; I'm having a bad day
        // and it seems like it's true right now.

        // TODO: Use the basis vectors to get rid of the adjust(...)
        // method and greatly shorten this code.

        //System.out.println("Trying to wrap " + c + ".");

        //System.out.println("   1. Reversing adjustments.");
        // Calculate old x and y adjustment.
        Coordinate invAdj = lattice.invAdjust(c);
        //System.out.println("      Inverse adjustment: " + invAdj);

        // Calculate adjustment delta.
        int dx = c.x() - invAdj.x();
        int dy = c.y() - invAdj.y();
        Coordinate delta = new Coordinate(dx, dy, Flags.VECTOR);

        //System.out.println("      Adjustment delta: " + delta);

        // Subtract old adjustments from coordinate.
        int x, y;

        x = c.x() - delta.x();
        y = c.y() - delta.y();

        Coordinate ua = new Coordinate(x, y, c.flags());
        //System.out.println("      Coordinate without adjustment: " + ua);

        //System.out.println("   2. Wrapping.");
        // Wrap.
        int xw = ua.x();
        while (xw >= width) {
            xw -= width;
        }

        while (xw < 0) {
            xw += width;
        }

        ////System.out.println("      Adjusted x to " + xw);

        // Apply adjustment again.
        Coordinate wrapped = new Coordinate(xw, y, c.flags());
        //System.out.println("   3. Wrapped coordinate: " + wrapped);

        Coordinate adjusted = lattice.adjust(wrapped);
        //System.out.println("   4. Adjusted, wrapped coordinate: " + adjusted);

        // Calculate new adjustment deltas.
        return adjusted.addFlags(Flags.BOUNDARY_APPLIED);
    }

    public Coordinate reflect(Coordinate c) {
        Coordinate ua = lattice.invAdjust(c);
        Coordinate refl = doReflection(ua);
        Coordinate adj = lattice.adjust(refl);
        return adj;
    }

    private Coordinate doReflection(Coordinate c) {
        //System.out.print(c);
        int x = c.x();
        int y = c.y();
        int flags = c.flags() | Flags.BOUNDARY_APPLIED;

        // Coordinate is above: reflect down.
        if (y >= height) {
            ////System.out.println(" A");
            int y1 = (2 * height) - y - 1;
            return doReflection(new Coordinate(x, y1, flags));
        }

        // Coordinate is below: reflect up.
        if (y < 0) {
            ////System.out.println(" B");
            int y1 = -1 * (y + 1);
            return doReflection(new Coordinate(x, y1, flags));
        }

        // Base case: coordinate is not above or below, so return it.
        ////System.out.println(" C");
        return c;
    }
}
