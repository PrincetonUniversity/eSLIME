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

package io.serialize;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * @test CoordinateSerializerTest
 * @author dbborens
 *
 */
public abstract class CoordinateSerializer {

	public static Element serialize(Coordinate coord) {
		String name;
		
		if (coord.hasFlag(Flags.VECTOR)) {
			name = "vector";
		} else {
			name = "coordinate";
		}
		
		return serialize(coord, name);
	}
	
	/**
	 * Note that only some tag names are legal for deserialization.
	 * Use caution when specifying custom tag names.
	 * 
	 * @param coord
	 * @param name
	 * @return
	 */
	public static Element serialize(Coordinate coord, String name) {
		Element e = new BaseElement(name);
		
		// All coordinates have these
		String x = Integer.toString(coord.x());
		String y = Integer.toString(coord.y());
		String f = Integer.toString(coord.flags());
		
		e.addAttribute("x", x);
		e.addAttribute("y", y);
		e.addAttribute("flags", f);
		
		if (!coord.hasFlag(Flags.PLANAR)) {
			String z = Integer.toString(coord.z());
			e.addAttribute("z", z);
		}
		
		return e;
	}
}
