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

package layers;

import cells.BehaviorCell;
import cells.Cell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.cell.CellLayer;
import layers.solute.LightweightSoluteLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dbborens on 3/26/14.
 */
public class LightweightSystemState extends SystemState {

    private double time;
    private int frame;
    private Map<Integer, Set<Coordinate>> highlights;

    private LayerManager layerManager;
    private Geometry geometry;

    public LightweightSystemState(Geometry geometry) {
        layerManager = new LayerManager();
        highlights = new HashMap<>();
        this.geometry = geometry;
    }


    public void setHighlights(Integer channelId, Set<Coordinate> sites) {
        highlights.put(channelId, sites);
    }

    @Override
    public LayerManager getLayerManager() {
        return layerManager;
    }

    @Override
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public boolean isHighlighted(int channel, Coordinate coord) {
        Set<Coordinate> highlightedSites = highlights.get(channel);
        return highlightedSites.contains(coord);
    }

    public void initCellLayer(int[] stateVector, double[] healthVector) {
        if (stateVector.length != geometry.getCanonicalSites().length) {
            throw new IllegalStateException("Actual number of data points not equal to expected number");
        }
        if (healthVector.length != geometry.getCanonicalSites().length) {
            throw new IllegalStateException("Actual number of data points not equal to expected number");
        }
        // Build cell layer.
        CellLayer cellLayer = new CellLayer(geometry);
        layerManager.setCellLayer(cellLayer);

        // Iterate over state vector.
        for (int i = 0; i < stateVector.length; i++) {

            // Convert index to coordinate.
            Coordinate coord = geometry.getCanonicalSites()[i];

            double health = healthVector[i];

            // If site is vacant, don't place anything
            int state = stateVector[i];
            if (state == 0) {
                continue;
            }

            // Build a dummy cell with the correct state and health.
            Cell cell = new BehaviorCell(layerManager, state, health, 0.0);

            // Place it in the cell layer.
            cellLayer.getUpdateManager().place(cell, coord);
        }

    }

    public void initSoluteLayer(String id, double[] soluteVector) {
        if (soluteVector.length != geometry.getCanonicalSites().length) {
            throw new IllegalStateException("Actual number of data points not equal to expected number");
        }
        LightweightSoluteLayer soluteLayer = new LightweightSoluteLayer(geometry, layerManager, id);
        for (int i = 0; i < soluteVector.length; i++) {
            Coordinate coord = geometry.getCanonicalSites()[i];
            double value = soluteVector[i];
            soluteLayer.set(coord, value);
        }

        layerManager.addSoluteLayer(id, soluteLayer);
    }
}
