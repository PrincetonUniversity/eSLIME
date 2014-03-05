/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package geometry.shape;

import structural.identifiers.Coordinate;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

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
