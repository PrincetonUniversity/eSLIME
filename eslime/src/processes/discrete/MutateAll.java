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

import cells.Cell;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell with a cell
 * state matching the target.
 *
 * @author dbborens
 */
public class MutateAll extends CellProcess {

    // How much biomass to accumulate per time step
    private int ancestral;
    private int mutant;

    public MutateAll(ProcessLoader loader, LayerManager layerManager, int id,
                     GeneralParameters p) {
        super(loader, layerManager, id, p);

        ancestral = Integer.valueOf(e.element("mutation").attribute("ancestral").getText());
        mutant = Integer.valueOf(e.element("mutation").attribute("mutant").getText());

    }

    public MutateAll(CellLayer layer, LayerManager layerManager,
                     int ancestral, int mutant) {
        super(null, layerManager, 0, null);

        this.ancestral = ancestral;
        this.mutant = mutant;
    }

    public void target(GillespieState gs) throws HaltCondition {
        // This process only has one event: it affects all relevant cells.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        // If the target state doesn't exist, don't waste any time
        // checking cells.
        int targetCount = layer.getViewer().getStateMapViewer().getCount(ancestral);
        if (targetCount == 0) {
            return;
        }

        // Feed the cells.
        for (Coordinate site : activeSites) {
            if (layer.getViewer().isOccupied(site) && layer.getViewer().getCell(site).getState() == ancestral) {
                Cell child = layer.getViewer().getCell(site).clone(mutant);
                layer.getUpdateManager().banish(site);
                layer.getUpdateManager().place(child, site);
            }
        }
    }

}
