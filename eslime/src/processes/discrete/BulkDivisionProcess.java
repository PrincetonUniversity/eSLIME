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
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLookupManager;
import layers.cell.CellUpdateManager;
import processes.MaxTargetHelper;
import processes.StepState;

import java.util.Random;
import java.util.Set;

public abstract class BulkDivisionProcess extends CellProcess {


    protected Random random;
    private Argument<Integer> maxTargets;
    private ShoveHelper shoveHelper;

    public BulkDivisionProcess(ProcessLoader loader, LayerManager layerManager, int id,
                               GeneralParameters p, Argument<Integer> maxTargets) {
        super(loader, layerManager, id, p);
        random = p.getRandom();
        this.maxTargets = maxTargets;

        shoveHelper = new ShoveHelper(layerManager, random);
    }

    protected void execute(Coordinate[] candidates) throws HaltCondition {
        Object[] chosen = MaxTargetHelper.respectMaxTargets(candidates, maxTargets.next(), p.getRandom());
        Cell[] chosenCells = toCellArray(chosen);
        for (int i = 0; i < chosenCells.length; i++) {
            Cell cell = chosenCells[i];
            CellLookupManager lm = layer.getLookupManager();
            Coordinate currentLocation = lm.getCellLocation(cell);
            doDivision(currentLocation);
        }

        // The shoving process complete, look for cells that have gotten pushed
        // out of bounds and remove them. If such a situation is not permitted
        // in this geometry, this loop merely iterates over an empty set.

    }

    private Cell[] toCellArray(Object[] chosen) {
        int n = chosen.length;
        Cell[] cells = new Cell[n];
        for (int i = 0; i < n; i++) {
            Coordinate coord = (Coordinate) chosen[i];
            Cell cell = layer.getViewer().getCell(coord);
            cells[i] = cell;
        }

        return cells;
    }



    protected void doDivision(Coordinate origin) throws HaltCondition {
        // Get child cell
        CellUpdateManager um = layer.getUpdateManager();
        Cell child = um.divide(origin);

        Coordinate target = shoveHelper.getTarget(origin);

        shoveHelper.shove(origin, target);

        // Divide the child cell into the vacancy left by the parent
        layer.getUpdateManager().place(child, origin);
    }



}
