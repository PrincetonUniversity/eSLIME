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

package structural.identifiers;

import structural.Flags;

public class TemporalCoordinate extends Coordinate {

	private double t;

	public TemporalCoordinate(int x, int y, double t, int flags) {
		super(x, y, flags);
		this.t = t;
	}
	
	public TemporalCoordinate(int x, int y, int z, double t, int flags) {
		super(x, y, z, flags);
		this.t = t;
	}

	public TemporalCoordinate(Coordinate c, double t) {
		super(c);
		this.t = t;
	}
	
	public double t() {
		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(t);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemporalCoordinate other = (TemporalCoordinate) obj;
		if (Double.doubleToLongBits(t) != Double.doubleToLongBits(other.t))
			return false;
		return true;
	}
	
	@Override
	protected String canonical(String open, String close, boolean useFlags) {
		StringBuilder ss = new StringBuilder();
		
		ss.append(open);
		ss.append(x);
		ss.append(", ");
		ss.append(y);
		
		if ((flags & Flags.PLANAR) == 0) {
			ss.append(", ");
			ss.append(z);
		}

		if (useFlags) {
			ss.append(" | ");
			ss.append(flags);
		}
		
		ss.append(" | ");
		ss.append(t);
		
		ss.append(close);
		
		String s = ss.toString();
		
		return s;
	}
}
