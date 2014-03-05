/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package cells;

import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.CellLookupManager;
import structural.identifiers.Coordinate;

/**
 * A helper class for cells that triggers
 * update events related to the life cycle
 * of the cell, such as death or divisibility.
 *
 * Created by dbborens on 2/21/14.
 */
public class CallbackManager {

    private Cell cell;
    private LayerManager layerManager;

    public CallbackManager(Cell cell, LayerManager layerManager) {
        this.cell = cell;
        this.layerManager = layerManager;
    }

    /**
     * Signals to the LayerManager that the callback cell is dead
     * and should be removed from the simulation.
     */
    public void die() {
        CellLayer layer = layerManager.getCellLayer();
        Coordinate coord = layer.getLookupManager().getCellLocation(cell);
        layer.getUpdateManager().banish(coord);
    }

    /**
     * Signals to the LayerManager that the callback cell may have
     * changed its divisibility status and should be checked.
     */
    public void refreshDivisibility() {
        CellLayer layer = layerManager.getCellLayer();

        if (layer.getViewer().exists(cell)) {
            Coordinate coord = layer.getLookupManager().getCellLocation(cell);
            layer.getUpdateManager().refreshDivisibility(coord);
        }
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }
}
