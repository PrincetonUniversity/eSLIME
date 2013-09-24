package io.parameters;

import geometries.*;

import org.dom4j.Element;

public abstract class GeometryFactory {

	public static Geometry make(ProjectLoader p) {
		Element baseElement = p.getElement("geometry");
		String className = get(baseElement, "class");
		
		Geometry g;
		
		if (className.equalsIgnoreCase("HexArena")) {
			int height = Integer.valueOf(get(baseElement, "height"));
			int width = Integer.valueOf(get(baseElement, "width"));
			g = new HexArena(height, width);
		} else if (className.equalsIgnoreCase("HexRing")) {
			int height = Integer.valueOf(get(baseElement, "height"));
			int width = Integer.valueOf(get(baseElement, "width"));
			g = new HexRing(height, width);
		} else {
			throw new IllegalArgumentException("Geometry class '" + className + "' not recognized.");
		}
		
		return g;
	}
	
	public static String get(Element e, String key) {
		Element kvp = e.element(key);
		Object data = kvp.getData();
		String dStr = data.toString();
		
		return dStr;
	}
}
