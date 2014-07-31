/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes;

import control.GeneralParameters;
import control.arguments.Argument;
import factory.control.arguments.DoubleArgumentFactory;
import factory.control.arguments.IntegerArgumentFactory;
import factory.geometry.set.CoordinateSetFactory;
import factory.processes.discrete.filter.FilterFactory;
import geometry.Geometry;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.MockProcess;
import processes.Process;
import processes.continuum.FieldUpdateProcess;
import processes.discrete.*;
import processes.discrete.check.*;
import processes.discrete.filter.Filter;
import processes.gillespie.GillespieProcess;
import processes.temporal.ExponentialInverse;
import processes.temporal.Tick;
import structural.utilities.XmlUtil;

/**
 * @author dbborens
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
            Argument<Double> dt = DoubleArgumentFactory.instantiate(e, "dt", 1.0, p.getRandom());
            return new Tick(loader, layerManager, id, p, dt);

        } else if (processClass.equalsIgnoreCase("divide-anywhere")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new DivideAnywhere(loader, layerManager, activeSites, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("active-layer-divide")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new ActiveLayerDivide(loader, layerManager, activeSites, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("occupied-neighbor-swap")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Integer> maxTargets = getMaxTargets(e, p);
            return new OccupiedNeighborSwap(loader, layerManager, activeSites, id, p, maxTargets);

        } else if (processClass.equalsIgnoreCase("general-neighbor-swap")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Integer> count = getCount(e, p);
            return new GeneralNeighborSwap(loader, layerManager, activeSites, id, p, count);

        } else if (processClass.equalsIgnoreCase("scatter")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new Scatter(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("fill")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new Fill(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("uniform-biomass-growth")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new UniformBiomassGrowth(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("targeted-biomass-growth")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new TargetedBiomassGrowth(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("mutate-all")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new MutateAll(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("mock-process")) {
            return new MockProcess(loader, layerManager, p, id);

        } else if (processClass.equalsIgnoreCase("gillespie-process")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new GillespieProcess(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("field-update-process")) {
            String target = e.element("target").getTextTrim();
            return new FieldUpdateProcess(loader, id, layerManager, p, target);

        } else if (processClass.equalsIgnoreCase("trigger")) {
            Filter filter = loadFilters(e);
            String behaviorName = XmlUtil.getString(e, "behavior");
            boolean skipVacant = XmlUtil.getBoolean(e, "skip-vacant-sites");
            int maxTargets = XmlUtil.getInteger(e, "max-targets", -1);
            boolean requireNeighbors = XmlUtil.getBoolean(e, "require-neighbors");
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new TriggerProcess(layerManager,
                    activeSites,
                    id,
                    behaviorName,
                    p,
                    filter,
                    skipVacant,
                    requireNeighbors,
                    maxTargets);
        } else if (processClass.equalsIgnoreCase("cull")) {
            double threshold = XmlUtil.getDouble(e, "threshold", 0.0);
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new Cull(loader, layerManager, activeSites, id, p, threshold);

        } else if (processClass.equalsIgnoreCase("diagnostic")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new DiagnosticProcess(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("check-for-fixation")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new CheckForFixation(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("check-threshold-occupancy")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Double> thresholdOccupancy = DoubleArgumentFactory.instantiate(e, "threshold", 1.0, p.getRandom());
            return new CheckForThresholdOccupancy(loader, layerManager, activeSites, id, p, thresholdOccupancy);

        } else if (processClass.equalsIgnoreCase("check-for-domination")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Double> thresholdFraction = DoubleArgumentFactory.instantiate(e, "threshold", p.getRandom());
            Argument<Integer> targetState = IntegerArgumentFactory.instantiate(e, "target", -1, p.getRandom());

            return new CheckForDomination(loader, layerManager, activeSites, id, p, targetState, thresholdFraction);

        } else if (processClass.equalsIgnoreCase("check-for-complete-fixation")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new CheckForCompleteFixation(loader, layerManager, activeSites, id, p);

        } else if (processClass.equalsIgnoreCase("check-for-extinction")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            Argument<Double> threshold = DoubleArgumentFactory.instantiate(e, "threshold", 0.0, p.getRandom());
            return new CheckForExtinction(loader, layerManager, activeSites, id, threshold, p);

        } else if (processClass.equalsIgnoreCase("record")) {
            CoordinateSet activeSites = getActiveSites(e, layerManager.getCellLayer().getGeometry(), p);
            return new Record(loader, layerManager, activeSites, id, p);

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

    private CoordinateSet getActiveSites(Element e, Geometry geom, GeneralParameters p) {
        Element sitesElem = e.element("active-sites");
        CoordinateSet ret = CoordinateSetFactory.instantiate(sitesElem, geom, p);
        return ret;
    }
}
