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


public class Extrema {

	// Minimum
	protected double min = Double.POSITIVE_INFINITY;
	protected TemporalCoordinate argMin = null;
	
	// Maximum
	protected double max = Double.NEGATIVE_INFINITY;
	protected TemporalCoordinate argMax = null;
	
	/**
	 * Initialize all fields to a set of loaded values. Used when importing
	 * metadata from a previous run.
	 * 
	 */
	public void load(double min, TemporalCoordinate argMin, double max, TemporalCoordinate argMax) {
		this.min = min;
		this.max = max;
		this.argMin = argMin;
		this.argMax = argMax;
	}
	
	/**
	 * Compares a value to the minimum and maximum. If it goes beyond an
	 * existing extremum, it gets assigned. Returns true if an assignment
	 * was made. If it is the first value checked, by definition 
	 * it will be both the minimum and maximum.
	 */
	public boolean consider(double u, Coordinate c, double t) {
		boolean assigned = false;
		if (u > max) {
			TemporalCoordinate tc = new TemporalCoordinate(c, t);
			max = u;
			argMax = tc; 
			assigned = true;
		}
		
		if (u < min) {
			TemporalCoordinate tc = new TemporalCoordinate(c, t);
			min = u;
			argMin = tc;
			assigned = true;
		}
		
		return assigned;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(min);
		sb.append('@');
		appendArgMin(sb);
		sb.append(':');
		sb.append(max);
		sb.append('@');
		appendArgMax(sb);
		return sb.toString();
	}

	private void appendArgMax(StringBuilder sb) {
		if (argMax == null) {
			sb.append("NaN,NaN,NaN");
			return;
		}
		
		sb.append(argMax.toString());
	}

	
	private void appendArgMin(StringBuilder sb) {
		if (argMin == null) {
			sb.append("NaN,NaN,NaN");
			return;
		}
		
		sb.append(argMin.toString());
	}
	
	public double min() {
		return min;
	}
	
	public double max() {
		return max;
	}
	
	public TemporalCoordinate argMin() {
		return argMin;
	}
	
	public TemporalCoordinate argMax() {
		return argMax;
	}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Extrema)) {
            return false;
        }

        Extrema other = (Extrema) obj;

        if (other.min != this.min) {
            return false;
        }

        if (other.max != this.max) {
            return false;
        }

        if (!(other.argMax.equals(this.argMax))) {
            return false;
        }

        if (!(other.argMin.equals(this.argMin))) {
            return false;
        }

        return true;
    }
}
