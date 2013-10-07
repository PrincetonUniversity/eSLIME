package processes;

import java.util.ArrayList;
import java.util.Set;

import org.dom4j.Element;

import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import geometries.Geometry;
import io.project.CellFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;

public abstract class Process {

	private int id;
	private int period;
	
	// XML element associated with this process
	private Element e;
	
	protected Geometry geom;
	
	/* Constructors */
	
	public Process(ProcessLoader loader, int id, Geometry geom) {
		
		if (loader == null) {
			System.err.println("WARNING: Mock behavior for process loader invoked. Use only for testing!");
			id = 0;
			period = 1;
			return;
		}
		
		this.id = id;
		e = loader.getProcess(id);
		period = Integer.valueOf(get("period", "1"));
		this.geom = geom;
	}

	/* Process identifiers */
	
	protected abstract String getProcessClass();

	public int getID() {
		return id;
	}
	
	/* Process events */
	
	public abstract void iterate(StepState state) throws HaltCondition;
	
	/**
	 *  Pull in a single-datum element
	 * @param g
	 * @param key
	 * @return
	 */
	private String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("Required parameter '" + 
					key + "' not defined for process " +
					getProcessClass() + " (id=" + getID() +").");
		}
		
		Object value = vElem.getData();
		
		return value.toString();
	}

	/**
	 * Parameter getter with no default value. If the
	 * parameter is omitted, an IllegalArgumentException
	 * will be thrown. Use for required parameters.
	 * 
	 * @param key
	 * @return
	 */
	protected String get(String key) {
		return get(e, key);
	}

	/**
	 * Parameter getter with a default value argument.
	 * Use for optional parameters.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected String get(String key, String defaultValue) {
		Element vElem = e.element(key);
		if (vElem == null) {
			return defaultValue;
		}
		
		Object value = vElem.getData();
		return value.toString();
	}
	public int getPeriod() {
		return period;
	}
	
	protected CellFactory getCellFactory(Lattice lattice) {
		return new CellFactory(lattice, e.element("cell-descriptor"));
	}
	
	/**
	 * Reads a site list descriptor of name elemName, which is a
	 * sub-element of the process element. Default is all canonical
	 * sites.
	 * 
	 * @param elemName
	 * @return
	 */
	protected Coordinate[] loadSiteList(String elemName) {
		Element siteElement = e.element(elemName);
		
		String modeString;
		
		// Default -- sites not restricted, so use all
		if (siteElement == null) {
			modeString = "all";
			
		// Otherwise, get the mode. If it doesn't exist, there
		// will be an error.
		} else {
			Element mode = siteElement.element("mode");
			modeString = mode.getText();
		}
		
		// Now, consider the mode.
		if (modeString.equalsIgnoreCase("all")) {
			return geom.getCanonicalSites();
		} else if (modeString.equalsIgnoreCase("list")) {
			return coordinateList(siteElement);
		} else if (modeString.equalsIgnoreCase("line")) {
			return coordinateLine(siteElement);
		} else {
			throw new IllegalArgumentException("Coordinate mode '" + modeString +"' not recognized.");
		}
	}

	private Coordinate[] coordinateRectangle(Element siteElement) {
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

		// Specifies one corner of the rectangle/prism. 
		Element originElem = siteElement.element("origin");
		
		// Specifies the delta in each direction to be used. May be negative.
		Element displacementElem = siteElement.element("displacement");
		
		// Specifies the total offset in each direction.
		Element dimensionsElem = siteElement.element("extension");
		
		boolean xyCorrection = siteElement.element("xy-correction") != null;
		
		// Use floating point so that w can build in offset corrections for
		// a particular geometry.
		double x0 = Double.valueOf(originElem.attribute("x").getValue());
		double y0 = Double.valueOf(originElem.attribute("y").getValue());
		
		double dx = Double.valueOf(displacementElem.attribute("dx").getValue());
		double dy = Double.valueOf(displacementElem.attribute("dy").getValue());
		
		double w = Double.valueOf(dimensionsElem.attribute("x").getValue());	
		double l = Double.valueOf(dimensionsElem.attribute("y").getValue());
		
		// 3D case
		if (originElem.attribute("z") != null) {
			double z0 = Double.valueOf(originElem.attribute("z").getValue());
			double h = Double.valueOf(dimensionsElem.attribute("z").getValue());
			double dz = Double.valueOf(displacementElem.attribute("dz").getValue());

			for (double x = x0; x < x0 + w; x += dx) {
				for (double y = y0; y < y0 + l; y += dy) {
					for (double z = z0; z < z0 + h; z += dz) {
						
						int xInt = (int) Math.round(Math.floor(x));
						int yInt = (int) Math.round(Math.floor(y));
						int zInt = (int) Math.round(Math.floor(z));

						if (xyCorrection) {
							yInt += xInt / 2;
						}
						
						Coordinate c = new Coordinate(xInt, yInt, zInt, 0);
						coordinates.add(c);
					}
				}
			}
			
		// 2D case
		} else {
			for (double x = x0; x < x0 + w; x += dx) {
				for (double y = y0; y < y0 + l; y += dy) {
					int xInt = (int) Math.round(Math.floor(x));
					int yInt = (int) Math.round(Math.floor(y));
					
					if (xyCorrection) {
						yInt += xInt / 2;
					}
					
					Coordinate c = new Coordinate(xInt, yInt, 0);
					coordinates.add(c);
				}
			}
		}

		return coordinates.toArray(new Coordinate[0]);
		
	}
	private Coordinate[] coordinateLine(Element siteElement) {
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		Element originElem = siteElement.element("origin");
		Element displacementElem = siteElement.element("displacement");
		
		int x0 = Integer.valueOf(originElem.attribute("x").getValue());
		int y0 = Integer.valueOf(originElem.attribute("y").getValue());
		
		int du = Integer.valueOf(displacementElem.attribute("u").getValue());
		int dv = Integer.valueOf(displacementElem.attribute("v").getValue());
		
		// Remember that 2D triangular geometries have a third, non-orthogonal
		// basis vector.
		int dw = 0;
		if (displacementElem.attribute("w") != null) {
			dw = Integer.valueOf(displacementElem.attribute("w").getValue());
		}
		
		int[] d = new int[] {du, dv, dw};
				
		int length = Integer.valueOf(siteElement.element("length").getText());
		
		Coordinate c;
		if (siteElement.attribute("z") != null) {
			int z0 = Integer.valueOf(originElem.element("z").getText());
			c = new Coordinate(x0, y0, z0, 0);
		} else {
			c = new Coordinate(x0, y0, 0);
		}
		
		coordinates.add(c);

		for (int i = 0; i < length; i++) {
			Coordinate cNext = geom.rel2abs(c, d);
			c = cNext;
			coordinates.add(c);
		}
		
		return coordinates.toArray(new Coordinate[0]);
	}

	private Coordinate[] coordinateList(Element siteElement) {
		
		System.out.println("Checking coordinate list for process " + id + ".");
		
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		for (Object o : siteElement.elements("coordinate")) {
			Element cElem = (Element) o;
			
			// All coordinates have x and y
			int x = Integer.valueOf(cElem.attribute("x").getValue());
			int y = Integer.valueOf(cElem.attribute("y").getValue());

			// Load flags, if any
			int flags = 0;
			if (cElem.attribute("flags") != null) {
				flags = Integer.valueOf(cElem.attribute("flags").getValue());
			}
			
			// If it has a z element, load 3D coordinate)
			if (cElem.attribute("z") != null) {
				int z = Integer.valueOf(cElem.attribute("z").getValue());
				Coordinate c = new Coordinate(x, y, z, flags);
				coordinates.add(c);
				
			} else {
				Coordinate c = new Coordinate(x, y, flags);
				coordinates.add(c);
			}
		}
		
		return coordinates.toArray(new Coordinate[0]);
	}
}
