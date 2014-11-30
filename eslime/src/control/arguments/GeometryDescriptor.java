/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * Created by dbborens on 11/24/14.
 */
public class GeometryDescriptor {

    private Lattice lattice;
    private Shape shape;

    public GeometryDescriptor(Lattice lattice, Shape shape) {
        this.shape = shape;
        this.lattice = lattice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeometryDescriptor that = (GeometryDescriptor) o;

        if (!lattice.equals(that.lattice)) return false;
        if (!shape.equals(that.shape)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lattice.hashCode();
        result = 31 * result + shape.hashCode();
        return result;
    }

    public Geometry make(Boundary boundary) {
        return new Geometry(lattice, shape, boundary);
    }

    public Lattice getLattice() {
        return lattice;
    }

    public Shape getShape() {
        return shape;
    }
}
