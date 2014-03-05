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
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * All boundaries are treated as infinite. However, going beyond
 * a specified limit results in the END_OF_WORLD flag being set.
 * This flag can then be handled within a specific process. In
 * practice, this amounts to a kind of absorbing boundary.
 * 
 * @test ArenaTest.java
 * @author David Bruce Borenstein
 *
 */
public class Arena extends Boundary {

	public Arena(Shape shape, Lattice lattice) {
		super(shape, lattice);
	}

	@Override
	public Coordinate apply(Coordinate c) {
		Coordinate ob = shape.getOverbounds(c);
		
		Coordinate ret;
		// Is any component overbound?
		if (ob.x() != 0 || ob.y() != 0 || ob.z() != 0) {
			// If yes, set the appropriate flags
			ret = c.addFlags(Flags.END_OF_WORLD | Flags.BOUNDARY_APPLIED);
		} else {
			ret = c;
		}

		return ret;
	}

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	protected void verify(Shape shape, Lattice lattice) {
		// Arena is compatible with all lattice geometries and 
		// shapes, so no verification is needed.
	}

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new Arena(scaledShape, clonedLattice);
    }
}
