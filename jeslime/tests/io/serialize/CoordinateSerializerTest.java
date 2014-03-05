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

import structural.Flags;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

public class CoordinateSerializerTest extends EslimeTestCase {

	public void testCoordinate() {
		Coordinate c2 = new Coordinate(1, 2, Flags.PLANAR);
		Coordinate c3 = new Coordinate(1, 2, 3, 4);
		
		Element e2 = CoordinateSerializer.serialize(c2);
		Element e3 = CoordinateSerializer.serialize(c3);
		
		doTest(c2, e2, "coordinate");
		doTest(c3, e3, "coordinate");
	}


	public void testVector() {
		Coordinate v2 = new Coordinate(1, 2, Flags.VECTOR | Flags.PLANAR);
		Coordinate v3 = new Coordinate(1, 2, 3, 4 | Flags.VECTOR);
		
		Element e2 = CoordinateSerializer.serialize(v2);
		Element e3 = CoordinateSerializer.serialize(v3);
		
		doTest(v2, e2, "vector");
		doTest(v3, e3, "vector");
	}

	/**
	 * Test the ability to specify arbitrary tag names. Note that only
	 * some custom tag names are legal for deserialization.
	 */
	public void testCustom() {
		Coordinate v2 = new Coordinate(1, 2, Flags.VECTOR | Flags.PLANAR);
		Coordinate v3 = new Coordinate(1, 2, 3, 4 | Flags.VECTOR);
		
		Element e2 = CoordinateSerializer.serialize(v2, "displacement");
		Element e3 = CoordinateSerializer.serialize(v3, "displacement");
		
		doTest(v2, e2, "displacement");
		doTest(v3, e3, "displacement");
	}
	
	private void doTest(Coordinate c, Element e, String name) {
		assertEquals(Integer.toString(c.x()), e.attribute("x").getValue());
		assertEquals(Integer.toString(c.y()), e.attribute("y").getValue());
		assertEquals(Integer.toString(c.flags()), e.attribute("flags").getValue());

		if (!c.hasFlag(Flags.PLANAR)) {
			assertEquals(Integer.toString(c.z()), e.attribute("z").getValue());
		}
		
		assertEquals(name, e.getName());
	}
}
