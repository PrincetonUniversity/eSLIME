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

package continuum.operations;

import structural.EpsilonUtil;
import structural.identifiers.Coordinate;
import geometry.Geometry;

public class Advection extends Operator {

	private Coordinate displacement;
	private double r;
	
	public Advection(Geometry geometry, boolean useBoundaries, Coordinate displacement, double r) {
		super(geometry, useBoundaries);
		this.displacement = displacement;
		this.r = r;
	}

	@Override
	public void init() {
		for (Coordinate origin : sites) {
			// Set origin to whatever isn't removed.
			int j = geometry.coordToIndex(origin);
			augment(j, j, 1.0 - r);
			
			// Now weight the target by r.
			Coordinate target = geometry.rel2abs(origin, displacement, mode());
			
			// Handle absorbing boundaries.
			if (target != null) {
				//System.out.println("   Trying to look up " + target);
				int i = geometry.coordToIndex(target);
				augment(i, j, r);
			}
		}
	}
	


	public double getR() {
		return r;
	}
	
	public Coordinate getDisplacement() {
		return displacement;
	}
}
