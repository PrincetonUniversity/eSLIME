/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLookupManager;
import layers.cell.CellUpdateManager;
import processes.MaxTargetHelper;

import java.util.Random;

public abstract class BulkDivisionProcess extends CellProcess {


    protected Random random;
    private Argument<Integer> maxTargets;
    private ShoveHelper shoveHelper;

    public BulkDivisionProcess(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                               GeneralParameters p, Argument<Integer> maxTargets) {
        super(loader, layerManager, activeSites, id, p);
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
        shoveHelper.removeImaginary();
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

        Coordinate target = shoveHelper.chooseVacancy(origin);

        shoveHelper.shove(origin, target);

        // Divide the child cell into the vacancy left by the parent
        layer.getUpdateManager().place(child, origin);
    }



}
