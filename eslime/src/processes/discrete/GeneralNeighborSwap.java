/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.Set;

/**
 * Neighbor swap -- switches a randomly chosen cell with one of
 * its neighbors.
 *
 * @author dbborens
 */
public class GeneralNeighborSwap extends CellProcess {

    private Coordinate[] activeSitesArr;

    public GeneralNeighborSwap(BaseProcessArguments arguments, CellProcessArguments cpArguments) {

        super(arguments, cpArguments);
    }

    @Override
    public void init() {
        activeSitesArr = new Coordinate[activeSites.size()];
        activeSites.toArray(activeSitesArr);
    }

    @Override
    public void fire(StepState state) throws HaltCondition {

        // Determine number of swaps to make.
        int n = maxTargets.next();


        // Perform swaps.
        for (int i = 0; i < n; i++) {
            // Choose first coordinate. Targets can be swapped multpiple times.
            int o = p.getRandom().nextInt(activeSitesArr.length);

            Coordinate first = activeSitesArr[o];

            System.out.println(first);
            Coordinate[] neighbors = layer.getGeometry().
                    getNeighbors(first, Geometry.APPLY_BOUNDARIES);

            int m =  p.getRandom().nextInt(neighbors.length);

            Coordinate second = neighbors[m];

            System.out.println(second);

            layer.getUpdateManager().swap(first, second);
        }

        // Remove any accumulated imaginary sites.
        removeImaginary();
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        if (gs != null) {
            gs.add(getID(), 1, 1.0);
        }
    }

    /**
     * Remove all out-of-bounds cells from the system.
     */
    private void removeImaginary() {
        Set<Coordinate> imaginarySites = layerManager.getCellLayer().getViewer().getImaginarySites();

        for (Coordinate c : imaginarySites) {
            layerManager.getCellLayer().getUpdateManager().banish(c);
        }
    }
}
