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

package processes.discrete;

import control.GeneralParameters;
import control.halt.HaltCondition;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.StateMapViewer;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Reports information about the model state. Useful for debugging
 * broken eSLIME scripts.
 * Created by dbborens on 3/7/14.
 */
public class DiagnosticProcess extends CellProcess {
    public DiagnosticProcess(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // Could add some debug information about the Gillespie state...
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        System.out.println("Occupied sites:" + layer.getViewer().getOccupiedSites().size());
        System.out.println("Divisible sites:" + layer.getViewer().getDivisibleSites().size());

        System.out.println("Cells by type:");
        StateMapViewer smv = layer.getViewer().getStateMapViewer();
        for (Integer s : smv.getStates()) {
            System.out.println("   type " + s + ": " + smv.getCount(s));
        }
    }
}
