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
import control.GeneralParameters;
import control.halt.HaltCondition;
import control.halt.LatticeFullEvent;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import layers.cell.CellLookupManager;
import layers.cell.CellUpdateManager;
import processes.MaxTargetHelper;
import processes.StepState;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class BulkDivisionProcess extends CellProcess {

    // This class is in need of a refactor.

    protected Random random;
    private Geometry geom;
    private int maxTargets;

//    private int debug = 0;
    public BulkDivisionProcess(ProcessLoader loader, LayerManager layerManager, int id,
                               GeneralParameters p, int maxTargets) {
        super(loader, layerManager, id, p);
        random = p.getRandom();
        geom = layer.getGeometry();
        this.maxTargets = maxTargets;
    }

    protected void execute(StepState state, Coordinate[] candidates) throws HaltCondition {
        Coordinate[] chosen = MaxTargetHelper.respectMaxTargets(candidates, maxTargets, p.getRandom());
        Cell[] chosenCells = toCellArray(chosen);
        for (int i = 0; i < chosenCells.length; i++) {
            Cell cell = chosenCells[i];
            CellLookupManager lm = layer.getLookupManager();
            Coordinate currentLocation = lm.getCellLocation(cell);
            doDivision(state, currentLocation);
        }

        // The shoving process complete, look for cells that have gotten pushed
        // out of bounds and remove them. If such a situation is not permitted
        // in this geometry, this loop merely iterates over an empty set.
        Set<Coordinate> imaginarySites = layer.getViewer().getImaginarySites();
        for (Coordinate c : imaginarySites) {
            layer.getUpdateManager().banish(c);
        }

    }

    private Cell[] toCellArray(Coordinate[] chosen) {
        int n = chosen.length;
        Cell[] cells = new Cell[n];
        for (int i = 0; i < n; i++) {
            Coordinate coord = chosen[i];
            Cell cell = layer.getViewer().getCell(coord);
            cells[i] = cell;
        }

        return cells;
    }


    private Coordinate getTarget(StepState state, Coordinate origin) throws HaltCondition {
//        if (origin.equals(new Coordinate(5, 7, 1))) {
//            System.out.println("Breakpoint");
//        }
        Coordinate target;
        // Get nearest vacancies to the cell
        Coordinate[] targets = layer.getLookupManager().getNearestVacancies(origin, -1);

//        System.out.println("DEBUG");
//        for (Coordinate t : targets) {
//            CellLayerViewer viewer = layer.getViewer();
//            Cell res = viewer.getCell(t);
//            if (res != null) {
//                throw new IllegalStateException(origin.toString());
//            }
//        }
        if (targets.length == 0) {
            throw new LatticeFullEvent(state.getTime());
        } else {
            int i = random.nextInt(targets.length);
            target = targets[i];
        }

        return target;
    }

    protected Coordinate getDisplacement(StepState state, Coordinate origin) throws HaltCondition {
        Coordinate target = getTarget(state, origin);

        // Move parent cell to the chosen vacancy, if necessary by shoving the
        // parent and a line of interior cells toward the surface
        Coordinate displacement = geom.getDisplacement(origin, target, Geometry.APPLY_BOUNDARIES);

        return displacement;
    }

    protected void doDivision(StepState state, Coordinate origin) throws HaltCondition {
        HashSet<Coordinate> affectedSites = new HashSet<>();

        // Get child cell
        CellUpdateManager um = layer.getUpdateManager();
        Cell child = um.divide(origin);

        Coordinate displacement = getDisplacement(state, origin);

        shove(origin, displacement, affectedSites);

        // Divide the child cell into the vacancy left by the parent
        layer.getUpdateManager().place(child, origin);
    }


    /**
     * @param currentLocation: starting location. the child will be placed in this
     *                position after the parent is shoved.
     * @param d:      displacement vector to target, in natural basis of lattice.
     * @param sites:  list of affected sites (for highlighting)
     *                <p/>
     *                TODO: This is so cloodgy and terrible.
     */
    private void shove(Coordinate currentLocation, Coordinate d, HashSet<Coordinate> sites) {

//        if (currentLocation.equals(new Coordinate(5, 2, 1))) {
//            System.out.println("Breakpoint");
//        }
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
        shove(nextLocation, du, sites);

        layer.getUpdateManager().swap(currentLocation, nextLocation);

        sites.add(nextLocation);
    }

    private Coordinate getNextLocation(Coordinate curLoc, Coordinate d, int nv, int[] dNext, int[] rel) {
        Coordinate nextLoc;
        int n = random.nextInt(nv);
        Coordinate disp = calcDisp(d, dNext, rel, n);
        nextLoc = geom.rel2abs(curLoc, disp, Geometry.APPLY_BOUNDARIES);
        return nextLoc;
    }

    private Coordinate calcDisp(Coordinate d, int[] dNext, int[] rel, int n) {
        // Initialize rel vector
        for (int i = 0; i < 3; i++) {
            rel[i] = 0;
        }

        // Decrement the displacement vector by one unit in a randomly chosen
        // direction, weighted so that the path is, on average, straight.
        if (n < Math.abs(d.x())) {
            dNext[0] -= (int) Math.signum(d.x());
            rel[0] += (int) Math.signum(d.x());
        } else if (n < (Math.abs(d.x()) + Math.abs(d.y()))) {
            dNext[1] -= (int) Math.signum(d.y());
            rel[1] += (int) Math.signum(d.y());
        } else {
            dNext[2] -= (int) Math.signum(d.z());
            rel[2] += (int) Math.signum(d.z());
        }

        return new Coordinate(rel, d.flags());
    }

}
