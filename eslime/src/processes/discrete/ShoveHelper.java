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

package processes.discrete;

import control.halt.HaltCondition;
import control.halt.LatticeFullEvent;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.StepState;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by dbborens on 5/14/14.
 */
public class ShoveHelper {

    private LayerManager layerManager;
    private Random random;

    public ShoveHelper(LayerManager layerManager, Random random) {
        this.layerManager = layerManager;
        this.random = random;
    }

    /**
     * Push the row of cells at origin toward target, such that origin
     * winds up vacant. Return a list of affected cells.
     *
     * @param origin The site to become vacant.
     * @param target A currently unoccupied site that will become occupied at
     *               the end of the shove process. The entire line of cells,
     *               including the cell at the origin, will have been pushed
     *               in the direction of the target.
     *
     * @return A set of coordinates that were affected by the shove operation.
     */
    public HashSet<Coordinate> shove(Coordinate origin, Coordinate target) {
        HashSet<Coordinate> affectedSites = new HashSet<>();

        Coordinate displacement = layerManager.getCellLayer().getGeometry().
                getDisplacement(origin,
                        target, Geometry.APPLY_BOUNDARIES);

        doShove(origin, displacement, affectedSites);
        return affectedSites;
    }

    /**
     * Remove all out-of-bounds cells from the system. Useful after a shoving
     * operation.
     */
    public void removeImaginary() {
        Set<Coordinate> imaginarySites = layerManager.getCellLayer().getViewer().getImaginarySites();

        for (Coordinate c : imaginarySites) {
            layerManager.getCellLayer().getUpdateManager().banish(c);
        }
    }

    /**
     * Gets the set of all nearest vacancies to the cell, and chooses randomly
     * between them.
     *
     * @param origin
     * @return
     * @throws HaltCondition
     */
    public Coordinate chooseVacancy(Coordinate origin) throws HaltCondition {
        StepState state = layerManager.getStepState();
        Coordinate target;
        // Get nearest vacancies to the cell
        Coordinate[] targets = layerManager.getCellLayer().getLookupManager().getNearestVacancies(origin, -1);
        if (targets.length == 0) {
            throw new LatticeFullEvent(state.getTime());
        } else {
            int i = random.nextInt(targets.length);
            target = targets[i];
        }

        return target;
    }

    /**
     * @param currentLocation: starting location. the child will be placed in this
     *                position after the parent is shoved.
     * @param d:      displacement vector to target, in natural basis of lattice.
     * @param sites:  list of affected sites (for highlighting)
     *                <p/>
     *                TODO: This is so cloodgy and terrible.
     */
    private void doShove(Coordinate currentLocation, Coordinate d, HashSet<Coordinate> sites) {

        // Base case 0: we've reached the target
        if (d.norm() == 0) {
            return;
        }

        // Choose whether to go horizontally or vertically, weighted
        // by the number of steps remaining in each direction
        int nv = d.norm();

        // Take a step in the chosen direction.
        int[] nextDisplacement;                // Displacement vector, one step closer
        Coordinate nextLocation;

        int[] rel = new int[3];            // Will contain a unit vector specifying
        // step direction

        // Loop if the move is illegal.
        do {

            nextDisplacement = new int[]{d.x(), d.y(), d.z()};

            nextLocation = getNextLocation(currentLocation, d, nv, nextDisplacement, rel);

            if (nextLocation == null) {
                continue;
            } else if (nextLocation.hasFlag(Flags.BEYOND_BOUNDS) && nv == 1) {
                throw new IllegalStateException("There's only one place to push cells and it's illegal!");
            } else if (!nextLocation.hasFlag(Flags.BEYOND_BOUNDS)) {
                break;
            }
        } while (true);

        Coordinate du = new Coordinate(nextDisplacement, d.flags());
        doShove(nextLocation, du, sites);

        layerManager.getCellLayer().getUpdateManager().swap(currentLocation,
                nextLocation);

        sites.add(nextLocation);
    }

    private Coordinate getNextLocation(Coordinate curLoc, Coordinate d, int nv, int[] dNext, int[] rel) {
        Coordinate nextLoc;
        int o = random.nextInt(nv);
        Coordinate disp = calcDisp(d, dNext, rel, o);
        nextLoc = layerManager.getCellLayer().getGeometry().rel2abs(curLoc,
                disp, Geometry.APPLY_BOUNDARIES);
        return nextLoc;
    }

    private Coordinate calcDisp(Coordinate d, int[] dNext, int[] rel, int o) {
        // Initialize rel vector
        for (int i = 0; i < 3; i++) {
            rel[i] = 0;
        }

        // Decrement the displacement vector by one unit in a randomly chosen
        // direction, weighted so that the path is, on average, straight.
        if (o < Math.abs(d.x())) {
            dNext[0] -= (int) Math.signum(d.x());
            rel[0] += (int) Math.signum(d.x());
        } else if (o < (Math.abs(d.x()) + Math.abs(d.y()))) {
            dNext[1] -= (int) Math.signum(d.y());
            rel[1] += (int) Math.signum(d.y());
        } else {
            dNext[2] -= (int) Math.signum(d.z());
            rel[2] += (int) Math.signum(d.z());
        }

        return new Coordinate(rel, d.flags());
    }

}
