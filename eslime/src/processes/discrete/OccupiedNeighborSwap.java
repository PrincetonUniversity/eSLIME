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

package processes.discrete;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.MaxTargetHelper;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Neighbor swap -- switches a randomly chosen cell with one of
 * its neighbors.
 *
 * @author dbborens
 */
public class OccupiedNeighborSwap extends CellProcess {

    private List<Object> candidates = null;
    private Argument<Integer> maxTargets;
    private Geometry geom;

    public OccupiedNeighborSwap(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                                GeneralParameters p, Argument<Integer> maxTargets) {

        super(loader, layerManager, activeSites, id, p);
        geom = layer.getGeometry();
        this.maxTargets = maxTargets;
    }

    @Override
    public void fire(StepState state) throws HaltCondition {

        if (candidates == null) {
            throw new IllegalStateException("Attempted to call fire() before calling target().");
        }

        if (candidates.size() == 0) {
            return;
        }

        //System.out.println("In NeighborSwap::iterate().");

        Object[] targets = selectTargets();

        if (targets.length == 0) {
            return;
        }

        for (Object tObj : targets) {
            SwapTuple target = (SwapTuple) tObj;
            layer.getUpdateManager().swap(target.p, target.q);
        }
        this.candidates = null;
    }

    private Object[] selectTargets() {

        Object[] selectedCoords = MaxTargetHelper.respectMaxTargets(candidates, maxTargets.next(), p.getRandom());


        return selectedCoords;
    }

    /**
     * Convenience class for pairs of coordinates to swap.
     */
    private class SwapTuple {
        public Coordinate p;
        public Coordinate q;

        public SwapTuple(Coordinate p, Coordinate q) {
            this.p = p;
            this.q = q;
        }
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {

        // Create an ArrayList of SwapTuples
        candidates = new ArrayList<>();

        // Get a list of occupied sites
        Set<Coordinate> coords = layer.getViewer().getOccupiedSites();

        // For each occupied site...
        for (Coordinate coord : coords) {

            // See how many occupied neighbors it has
            Coordinate[] neighbors = geom.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);

            // Add each possible swap as a candidate
            for (Coordinate neighbor : neighbors) {
                if (layer.getViewer().isOccupied(neighbor)) {
                    SwapTuple sw = new SwapTuple(coord, neighbor);
                    candidates.add(sw);
                }
            }

        }

        // Weight is defined in terms of number of swappable cells,
        // whereas event count is defined in terms of number of possible
        // swaps. Since every swap can occur in one of two directions,
        // we halve the number of swaps.
        if (gs != null) {
            gs.add(getID(), candidates.size() / 2, coords.size() * 1.0D);
        }
    }
}
