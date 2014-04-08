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

package agent.targets;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayerViewer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetVacantNeighbors extends TargetRule {

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetVacantNeighbors(child, layerManager, maximum, random);
    }

    public TargetVacantNeighbors(BehaviorCell callback, LayerManager layerManager, int maximum, Random random) {
        super(callback, layerManager, maximum, random);
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get cell layer viewer
        CellLayerViewer viewer = layerManager.getCellLayer().getViewer();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        // Create an array list of neighbors that are vacant
        ArrayList<Coordinate> vacNeighbors = new ArrayList<>(neighbors.length);

        for (Coordinate neighbor : neighbors) {
            if (!viewer.isOccupied(neighbor)) {
                vacNeighbors.add(neighbor);
            }
        }

        // Convert to array
        Coordinate[] ret = vacNeighbors.toArray(new Coordinate[0]);

        // Return the array
        return ret;
    }
}
