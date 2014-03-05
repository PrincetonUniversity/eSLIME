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

package layers.solute;

import structural.identifiers.Coordinate;

/**
 * TODO this is redundant with Extrema
 */
public class Extremum implements Comparable {

	private double value;
	private Coordinate argument;
	private double time;
	
	public Extremum(Coordinate argument, double time, double value) {
		this.value = value;
		this.argument = argument;
		this.time = time;
	}
	
	public double getValue() {
		return value;
	}

	public Coordinate getArgument() {
		return argument;
	}

	public double getTime() {
		return time;
	}

	@Override
	public int compareTo(Object other) {
		Extremum o = (Extremum) other;
		
		if (this.value > o.value) {
			return 1;
		}
		
		if (this.value < o.value) {
			return -1;
		}
		
		return 0;
	}
	
}
