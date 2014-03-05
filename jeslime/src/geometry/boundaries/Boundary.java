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

package geometry.boundaries;

import geometry.lattice.Lattice;
import geometry.shape.Shape;
import structural.identifiers.Coordinate;

public abstract class Boundary {

	protected Shape shape;
	protected Lattice lattice;
	
	public Boundary(Shape shape, Lattice lattice) {
		verify(shape, lattice);
		this.shape = shape;
		this.lattice = lattice;
	}
	
	protected abstract void verify(Shape shape, Lattice lattice);
	
	public abstract Coordinate apply(Coordinate c);
	
	/**
	 * If false, we are allowed to check against the size of the canonical site
	 * array to determine the number of legal lattice sites. If false, we should
	 * treat the number of legal lattice sites as infinite. 
	 * 
	 * Note that, if an infinite number of sites is promised, this promise must
	 * be kept (for annulus, etc.) or else jeSLIME is likely to enter infinite
	 * loop conditions and hang.
	 * 
	 * @return
	 */
	public abstract boolean isInfinite();

    /**
     * Boundaries are considered equal if and only if they are the
     * same class. Equality does NOT require that boundaries are being
     * applied to the same shape or lattice!
     *
     * If a boundary requires additional arguments (such as a constant
     * flux rate or some other parameter), then the child class should
     * override the equality operator, first invoking this equality
     * operator.
     */
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        return true;
    }

    public abstract Boundary clone(Shape scaledShape, Lattice clonedLattice);
}
