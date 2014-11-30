/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.cell.CellLookupManager;
import layers.cell.CellUpdateManager;
import processes.BaseProcessArguments;
import processes.MaxTargetHelper;

import java.util.Random;

public abstract class BulkDivisionProcess extends CellProcess {


    protected Random random;
    private ShoveHelper shoveHelper;

    public BulkDivisionProcess(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments, cpArguments);
    }

    @Override
    public void init() {
        random = p.getRandom();
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
