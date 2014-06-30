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

package processes.discrete.check;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.ExtinctionEvent;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForExtinction extends CellProcess {

    private double threshold;

    public CheckForExtinction(ProcessLoader loader, LayerManager layerManager, int id, Argument<Double> thresholdArg, GeneralParameters p) {
        super(loader, layerManager, id, p);
        threshold = thresholdArg.next();
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen in this process.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        // Handle true extinction exactly
        if (p.epsilonEquals(threshold, 0.0) && layer.getViewer().getOccupiedSites().size() == 0) {
            throw new ExtinctionEvent(state.getTime());
        }

        double totalSites = layer.getGeometry().getCanonicalSites().length * 1.0;
        double sitesOccupied = layer.getViewer().getOccupiedSites().size() * 1.0;

        double occupancy = sitesOccupied / totalSites;

        if (occupancy < threshold) {
            throw new ExtinctionEvent(state.getTime());
        }
    }
}
