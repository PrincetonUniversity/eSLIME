package io.project;

import layers.LayerManager;
import org.dom4j.Element;

import processes.MockProcess;
import processes.Process;
import processes.continuum.FieldUpdateProcess;
import processes.discrete.*;
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

    private final LayerManager layerManager;
    private ProcessLoader loader;
	private GeneralParameters p;

	public ProcessFactory(ProcessLoader loader, LayerManager lm, GeneralParameters p) {
		this.loader = loader;
		this.layerManager = lm;
		this.p = p;

	}

    public Process[] getProcesses() {
        Integer[] ids = loader.getProcesses();
        Process[] processes = new Process[ids.length];

        // Build processes.
        for (int i = 0; i < ids.length; i++) {
            int id = ids[i];
            processes[i] = instantiate(id);
        }

        return processes;
    }

	public Process instantiate(Integer id) {
		Element e = loader.getProcess(id);

        // TODO Change interface on old cell layers so that they can find
        // their own layer and geometry from the layermanager
        CellLayer layer = layerManager.getCellLayer();
        Geometry geom = layerManager.getCellLayer().getGeometry();

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
			return new MockProcess(loader, id, geom);
			
		} else if (processClass.equalsIgnoreCase("gillespie-process")) {
			return new GillespieProcess(loader, layer, id, geom, p, layerManager);

        } else if (processClass.equalsIgnoreCase("field-update-process")) {
            String target = e.element("target").getTextTrim();
            return new FieldUpdateProcess(loader, id, layerManager, target);

		} else {
			String msg = "Unrecognized process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}		
	}
}
