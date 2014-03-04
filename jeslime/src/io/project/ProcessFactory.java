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

		String processClass = e.getName();
		
		if (processClass.equalsIgnoreCase("exponential-inverse")) {
			return new ExponentialInverse(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("tick")) {
			return new Tick(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("divide-anywhere")) {
            int maxTargets = getMaxTargets(e);
			return new DivideAnywhere(loader, layerManager, id, p, maxTargets);
			
		} else if (processClass.equalsIgnoreCase("active-layer-divide")) {
                int maxTargets = getMaxTargets(e);
				return new ActiveLayerDivide(loader, layerManager, id, p, maxTargets);
				
		} else if (processClass.equalsIgnoreCase("neighbor-swap")) {
			return new NeighborSwap(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("scatter")) {
			return new Scatter(loader, layerManager, id, p);

		} else if (processClass.equalsIgnoreCase("fill")) {
			return new Fill(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("uniform-biomass-growth")) {
			return new UniformBiomassGrowth(loader, layerManager, id, p);

		} else if (processClass.equalsIgnoreCase("targeted-biomass-growth")) {
			return new TargetedBiomassGrowth(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("mutate-all")) {
			return new MutateAll(loader, layerManager, id, p);
			
		} else if (processClass.equalsIgnoreCase("mock-process")) {
			return new MockProcess(loader, layerManager, p, id);
			
		} else if (processClass.equalsIgnoreCase("gillespie-process")) {
			return new GillespieProcess(loader, layerManager, id, p);

        } else if (processClass.equalsIgnoreCase("field-update-process")) {
            String target = e.element("target").getTextTrim();
            return new FieldUpdateProcess(loader, id, layerManager, p, target);

        } else if (processClass.equalsIgnoreCase("trigger")) {
            return new Trigger(loader, layerManager, id, p);

		} else {
			String msg = "Unrecognized process '" +
					processClass + "' (id=" + id + ").";
			
			throw new IllegalArgumentException(msg);
		}		
	}

    private int getMaxTargets(Element e) {
        Element maxElem = e.element("max-targets");
        if (maxElem == null) {
            return -1;
        }

        String maxStr = maxElem.getTextTrim();
        Integer maxTargets = Integer.valueOf(maxStr);
        return maxTargets;
    }
}
