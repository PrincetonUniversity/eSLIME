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
	
	protected abstract String getElementName();

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
		} else {
			throw new IllegalArgumentException("Coordinate mode '" + modeString +"' not recognized.");
		}
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
