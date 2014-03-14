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
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.identifiers.Coordinate;

public class PlaneRingReflecting extends Boundary {

    private PlaneRingHelper helper;

    public PlaneRingReflecting(Shape shape, Lattice lattice) {
        super(shape, lattice);
        helper = new PlaneRingHelper(lattice, shape.getDimensions());
    }

    @Override
    public Coordinate apply(Coordinate c) {
        Coordinate ob = shape.getOverbounds(c);

        // First, fix x if needed.
        Coordinate wrapped, reflected;

        if (ob.x() != 0) {
            wrapped = helper.wrap(c);
        } else {
            wrapped = c;
        }

        // Next, fix y if needed.
        if (ob.y() != 0) {
            reflected = helper.reflect(wrapped);
        } else {
            reflected = wrapped;
        }

        return reflected;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    protected void verify(Shape shape, Lattice lattice) {
        // PlaneRing is compatible only with Rectangle shapes.
        if (!(shape instanceof Rectangle)) {
            throw new IllegalArgumentException("PlaneRingReflecting boundary requires a Rectangle shape.");
        }
    }

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new PlaneRingReflecting(scaledShape, clonedLattice);
    }
}
