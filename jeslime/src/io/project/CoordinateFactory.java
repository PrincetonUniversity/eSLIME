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

package io.project;

import geometry.Geometry;
import org.dom4j.Element;

import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * 
 * @test CoordinateFactoryTest
 * @author dbborens
 *
 */
public abstract class CoordinateFactory {

	public static Coordinate instantiate(Element e) {
		// Coordinate or vector?
		int flags = processName(e);
		
		// All coordinates have x and y
		int x = Integer.valueOf(e.attribute("x").getValue());
		int y = Integer.valueOf(e.attribute("y").getValue());

		// Process flags, if any
		if (e.attribute("flags") != null) {
			flags |= Integer.valueOf(e.attribute("flags").getValue());
		}
		
		// If it has a z element, load 3D coordinate)
		if (e.attribute("z") != null) {
			int z = Integer.valueOf(e.attribute("z").getValue());
			Coordinate c = new Coordinate(x, y, z, flags);
			return c;
			
		} else {
			Coordinate c = new Coordinate(x, y, flags);
			return c;
		}
	}

	protected static int processName(Element e) {
		int flags;
		
		if (e.getName().equalsIgnoreCase("coordinate")) {
			flags = 0;
			
		} else if (e.getName().equalsIgnoreCase("vector")) {
			flags = Flags.VECTOR;
			
		// I kind of don't like having "displacement" defined as its own
		// "vector" flag, but I don't know how else to do it.
		} else if (e.getName().equalsIgnoreCase("displacement")) {
			flags = Flags.VECTOR;

        // "offset" tags are used to specify distances from the center
        // coordinate.
        } else if (e.getName().equalsIgnoreCase("offset")) {
            flags = Flags.VECTOR;

		} else {
			throw new IllegalArgumentException("Unrecognized coordinate tag '" + 
					e.getName() + "'.");
		}
		
		return flags;
	}
	
	public static Coordinate instantiate(Object o) {
		if (o instanceof Element) {
			Element e = (Element) o;
			return instantiate(e);
		} else {
			throw new IllegalArgumentException("Cannot build a coordinate from class " 
					+ o.getClass().getSimpleName());
		}
	}

    public static Coordinate offset(Object o, Geometry geom) {
        Coordinate displacement = instantiate(o);
        Coordinate origin = geom.getCenter();
        return geom.rel2abs(origin, displacement, Geometry.APPLY_BOUNDARIES);
    }
}
