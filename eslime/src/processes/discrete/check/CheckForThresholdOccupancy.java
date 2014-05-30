/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

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
import control.halt.FixationEvent;
import control.halt.HaltCondition;
import control.halt.LatticeFullEvent;
import control.halt.ThresholdOccupancyReachedEvent;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.StateMapViewer;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForThresholdOccupancy extends CellProcess {
    private int thresholdCount;
    public CheckForThresholdOccupancy(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p, Argument<Double> thresholdOccupancy) {
        super(loader, layerManager, id, p);
        double toVal = thresholdOccupancy.next();
        if (toVal > 1.0 || toVal < 0) {
            throw new IllegalArgumentException("Illegal occupancy fraction " + toVal);
        }

        thresholdCount = (int) Math.floor(layer.getGeometry().getCanonicalSites().length * toVal);
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
        int numOccupied = layer.getViewer().getOccupiedSites().size();

        if (numOccupied >= thresholdCount) {
            throw new ThresholdOccupancyReachedEvent(state.getTime());
        }
    }
}
