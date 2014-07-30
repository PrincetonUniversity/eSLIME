/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.shape;

import control.identifiers.Coordinate;
import geometry.lattice.Lattice;

public class MockShape extends Shape {

    public MockShape() {
        super(null);
    }

    @Override
    protected void verify(Lattice lattice) {

    }

    @Override
    public Coordinate getCenter() {
        return null;
    }

    @Override
    public Coordinate[] getBoundaries() {
        return null;
    }

    @Override
    public Coordinate getOverbounds(Coordinate coord) {
        return null;
    }

    @Override
    protected Coordinate[] calcSites() {
        return null;
    }

    @Override
    public int[] getDimensions() {
        return null;
    }

    @Override
    public Coordinate[] getCanonicalSites() {
        return null;
    }

    // We want to be able to declare mock objects "equal"
    private boolean equal;

    public void setEqual(boolean equal) {
        this.equal = equal;
    }

    @Override
    public boolean equals(Object obj) {
        return equal;
    }

    @Override
    public Shape cloneAtScale(Lattice clonedLattice, double rangeScale) {
        return new MockShape();
    }
}
