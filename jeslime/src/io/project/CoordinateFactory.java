package io.project;

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
}
