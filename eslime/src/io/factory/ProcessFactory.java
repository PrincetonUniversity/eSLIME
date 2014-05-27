/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.factory;

import control.GeneralParameters;
import control.arguments.Argument;
import io.loader.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.MockProcess;
import processes.Process;
import processes.continuum.FieldUpdateProcess;
import processes.discrete.*;
import processes.discrete.filter.Filter;
import processes.gillespie.GillespieProcess;
import processes.temporal.ExponentialInverse;
import processes.temporal.Tick;
import structural.utilities.XmlUtil;

/**
 * @author dbborens
 * @untested
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
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new DivideAnywhere(loader, layerManager, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("active-layer-divide")) {
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new ActiveLayerDivide(loader, layerManager, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("occupied-neighbor-swap")) {
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new OccupiedNeighborSwap(loader, layerManager, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("general-neighbor-swap")) {
            Argument<Integer> count = getCount(e, p);
            return new GeneralNeighborSwap(loader, layerManager, id, p, count);

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
            Filter filter = loadFilters(e);
            String behaviorName = XmlUtil.getString(e, "behavior");
            boolean skipVacant = XmlUtil.getBoolean(e, "skip-vacant-sites");
            int maxTargets = XmlUtil.getInteger(e, "max-targets", -1);
            boolean requireNeighbors = XmlUtil.getBoolean(e, "require-neighbors");
            return new TriggerProcess(layerManager,
                    id,
                    behaviorName,
                    p,
                    filter,
                    skipVacant,
                    requireNeighbors,
                    maxTargets);
        } else if (processClass.equalsIgnoreCase("cull")) {
            double threshold = XmlUtil.getDouble(e, "threshold", 0.0);
            return new Cull(loader, layerManager, id, p, threshold);

        } else if (processClass.equalsIgnoreCase("diagnostic")) {
            return new DiagnosticProcess(loader, layerManager, id, p);

        } else if (processClass.equalsIgnoreCase("check-for-fixation")) {
            return new CheckForFixation(loader, layerManager, id, p);

        } else if (processClass.equalsIgnoreCase("check-for-complete-fixation")) {
            return new CheckForCompleteFixation(loader, layerManager, id, p);

        } else if (processClass.equalsIgnoreCase("check-for-extinction")) {
            return new CheckForExtinction(loader, layerManager, id, p);

        } else if (processClass.equalsIgnoreCase("record")) {
            return new Record(loader, layerManager, id, p);

        } else {
            String msg = "Unrecognized process '" +
                    processClass + "' (id=" + id + ").";

            throw new IllegalArgumentException(msg);
        }
    }

    private Filter loadFilters(Element root) {
        Element e = root.element("filters");
        Filter filter = FilterFactory.instantiate(e, layerManager, p);
        return filter;
    }

    private Argument<Integer> getMaxTargets(Element e, GeneralParameters p) {
        return IntegerArgumentFactory.instantiate(e, "max-targets", -1, p.getRandom());
    }

    private Argument<Integer> getCount(Element e, GeneralParameters p) {
        return IntegerArgumentFactory.instantiate(e, "count", p.getRandom());
    }
}
