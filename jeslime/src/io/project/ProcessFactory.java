package io.project;

import org.dom4j.Element;

import processes.Process;
import processes.cellular.*;
import processes.temporal.*;

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
		
		String processClass = e.getName();
		
		if (processClass.equalsIgnoreCase("exponential-inverse")) {
			return new ExponentialInverse(loader, lattice, id, geom, p);
		} else if (processClass.equalsIgnoreCase("divide-anywhere")) {
			return new DivideAnywhere(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("active-layer-divide")) {
				return new ActiveLayerDivide(loader, lattice, id, geom, p);		
				
		} else if (processClass.equalsIgnoreCase("neighbor-swap")) {
			return new NeighborSwap(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("scatter")) {
			return new Scatter(loader, lattice, id, geom, p);

		} else if (processClass.equalsIgnoreCase("fill")) {
			return new Fill(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("uniform-biomass-growth")) {
			return new UniformBiomassGrowth(loader, lattice, id, geom, p);

		} else if (processClass.equalsIgnoreCase("targeted-biomass-growth")) {
			return new TargetedBiomassGrowth(loader, lattice, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("mutate-all")) {
			return new MutateAll(loader, lattice, id, geom, p);
			
		} else {
			String msg = "Unrecognized process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}		
	}
}
