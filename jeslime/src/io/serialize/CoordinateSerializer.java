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
