/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellUpdateManager;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.ArrayList;

/**
 * Kill all cells whose biomass is less than a defined
 * threshold.
 * <p/>
 * Created by dbborens on 3/5/14.
 */
public class Cull extends CellProcess {
    private Coordinate[] targetsArr = null;
    private double threshold;

    public Cull(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                GeneralParameters p, double threshold) {
        super(loader, layerManager, activeSites, id, p);
        this.threshold = threshold;
    }

    public void target(GillespieState gs) throws HaltCondition {

        ArrayList<Coordinate> targets = new ArrayList<>();
        for (Coordinate candidate : activeSites) {
            if (!layer.getViewer().isOccupied(candidate)) {
                continue;
            }

            Cell cell = layer.getViewer().getCell(candidate);
            if (cell.getHealth() <= threshold) {
                targets.add(candidate);
            }
        }

        targetsArr = targets.toArray(new Coordinate[0]);

        if (gs != null) {
            gs.add(getID(), targets.size(), targets.size() * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        execute(state, targetsArr);
        targetsArr = null;
    }

    private void execute(StepState state, Coordinate[] targetsArr) {
        CellUpdateManager manager = layer.getUpdateManager();
        for (Coordinate target : targetsArr) {
            manager.banish(target);
        }
    }
}
