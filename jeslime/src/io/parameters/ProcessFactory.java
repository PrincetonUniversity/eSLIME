package io.parameters;

import org.dom4j.Element;

import processes.Process;
import processes.cellular.CellProcess;
import processes.cellular.DivideAnywhere;
import processes.cellular.NeighborSwap;
import processes.cellular.Scatter;
import processes.temporal.PopulationTrack;
import processes.temporal.TimeProcess;

import structural.GeneralParameters;
import structural.Lattice;
import geometries.Geometry;

public class ProcessFactory {

	private ProcessLoader loader;
	private Lattice lattice;
	private GeneralParameters p;
	private Geometry geom;
	
	public ProcessFactory(ProcessLoader loader, Lattice lattice, GeneralParameters p, Geometry geom) {
		this.loader = loader;
		this.lattice = lattice;
		this.p = p;
		this.geom = geom;
	}
	
	public Process instantiate(Integer id) {
		Element e = loader.getProcess(id);
		
		String type = e.getName();
		
		if (type.equalsIgnoreCase("cell-process")) {
			return cellProcess(e);
		} else if (type.equalsIgnoreCase("time-process")) {
			return temporalProcess(e);
		} else if (type.equalsIgnoreCase("continuum-process")) {
			throw new UnsupportedOperationException("Write me!");
		} else {
			String msg = "Unrecognized process type '" + type + "' (id=" + id + ").";
			throw new IllegalArgumentException(msg);
		}
		
	}

	private TimeProcess temporalProcess(Element e) {
		String processClass = get(e, "class");

		Integer id = Integer.valueOf(get(e, "id"));

		if (processClass.equalsIgnoreCase("PopulationTrack")) {
			return new PopulationTrack(loader, lattice, id, geom, p);	
		} else {
			String msg = "Unrecognized time process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}
	}

	private CellProcess cellProcess(Element e) {
		String processClass = get(e, "class");
		
		Integer id = Integer.valueOf(get(e, "id"));
		
		if (processClass.equalsIgnoreCase("DivideAnywhere")) {
			return new DivideAnywhere(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("NeighborSwap")) {
			return new NeighborSwap(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("Scatter")) {
			return new Scatter(loader, lattice, id, geom, p);
			
		} else {
			String msg = "Unrecognized cell process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}
	}
	
	// Pull in a single-datum element
	private String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("General parameter " + 
					key + " not defined.");
		}
		
		Object value = vElem.getData();
		
		return value.toString();
			
	}
}
