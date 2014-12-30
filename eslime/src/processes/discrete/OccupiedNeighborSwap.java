/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import processes.BaseProcessArguments;
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

    private List<Object> candidates;
    private Geometry geom;

    public OccupiedNeighborSwap(BaseProcessArguments arguments, CellProcessArguments cpArguments) {

        super(arguments, cpArguments);
        geom = layer.getGeometry();
    }

    @Override
    public void init() {
        candidates = null;
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
            System.out.println("Swapping" + target.p + " with " + target.q);
            layer.getUpdateManager().swap(target.p, target.q);
        }
        this.candidates = null;
    }

    private Object[] selectTargets() throws HaltCondition {

        Object[] selectedCoords = MaxTargetHelper.respectMaxTargets(candidates, maxTargets.next(), p.getRandom());


        return selectedCoords;
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
}
