package jeslime.io.project;

import org.dom4j.Element;

import processes.NullProcess;
import processes.Process;
import processes.cellular.*;
import processes.gillespie.GillespieProcess;
import processes.temporal.*;
import structural.GeneralParameters;
import layers.cell.CellLayer;
import geometry.Geometry;

/**
 * @untested 
 * @author dbborens
 *
 */
public class ProcessFactory {

	private ProcessLoader loader;
	private CellLayer layer;
	private GeneralParameters p;
	private Geometry geom;
	
	public ProcessFactory(ProcessLoader loader, CellLayer layer, GeneralParameters p, Geometry geom) {
		this.loader = loader;
		this.layer = layer;
		this.p = p;
		this.geom = geom;
	}

	public Process instantiate(Integer id) {
		Element e = loader.getProcess(id);
		
		String processClass = e.getName();
		
		if (processClass.equalsIgnoreCase("exponential-inverse")) {
			return new ExponentialInverse(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("tick")) {
			return new Tick(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("divide-anywhere")) {
			return new DivideAnywhere(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("active-layer-divide")) {
				return new ActiveLayerDivide(loader, layer, id, geom, p);		
				
		} else if (processClass.equalsIgnoreCase("neighbor-swap")) {
			return new NeighborSwap(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("scatter")) {
			return new Scatter(loader, layer, id, geom, p);

		} else if (processClass.equalsIgnoreCase("fill")) {
			return new Fill(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("uniform-biomass-growth")) {
			return new UniformBiomassGrowth(loader, layer, id, geom, p);

		} else if (processClass.equalsIgnoreCase("targeted-biomass-growth")) {
			return new TargetedBiomassGrowth(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("mutate-all")) {
			return new MutateAll(loader, layer, id, geom, p);
			
		} else if (processClass.equalsIgnoreCase("null-process")) {
			return new NullProcess(loader, id, geom);
			
		} else if (processClass.equalsIgnoreCase("gillespie-process")) {
			return new GillespieProcess(loader, layer, id, geom, p);
			
		} else {
			String msg = "Unrecognized process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}		
	}
}
