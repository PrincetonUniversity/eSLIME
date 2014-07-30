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

    private Argument<Integer> count;
    private Coordinate[] activeSitesArr;

    public GeneralNeighborSwap(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                               GeneralParameters p, Argument<Integer> count) {

        super(loader, layerManager, activeSites, id, p);
        this.count = count;

        activeSitesArr = new Coordinate[activeSites.size()];
        activeSites.toArray(activeSitesArr);
    }

    @Override
    public void fire(StepState state) throws HaltCondition {

        // Determine number of swaps to make.
        int n = count.next();


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
