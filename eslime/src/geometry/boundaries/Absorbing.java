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
import geometry.shape.Shape;
import structural.identifiers.Coordinate;

/**
 * A boundary condition that returns null for any overbound
 * position. Since Geometry removes null values, the result
 * is that cells adjacent to the boundary have fewer neighbors
 * than cells at the interior.
 * <p/>
 * Created by David B Borenstein on 12/22/13.
 */
public class Absorbing extends Boundary {

    public Absorbing(Shape shape, Lattice lattice) {
        super(shape, lattice);
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        Coordinate ret;
        // Is any component overbound?
        if (ob.x() != 0 || ob.y() != 0 || ob.z() != 0) {
            // If yes return null
            ret = null;

        } else {
            // Otherwise, just return the coordinate.
            ret = c;
        }

        return ret;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        // Absorbing is compatible with all lattice geometries and
        // shapes, so no verification is needed.
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new Absorbing(scaledShape, clonedLattice);
    }
}
