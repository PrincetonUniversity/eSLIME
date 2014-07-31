/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.check;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.DominationEvent;
import control.halt.HaltCondition;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.discrete.CellProcess;
import processes.gillespie.GillespieState;

/**
 * Halt the simulation when the target cell type has the specified fraction
 * of the overall live cell population.
 *
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForDomination extends CellProcess {
    private double targetFraction;
    private int targetState;

    public CheckForDomination(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p, Argument<Integer> targetStateArg, Argument<Double> targetFractionArg) {
        super(loader, layerManager, activeSites, id, p);
        targetFraction = targetFractionArg.next();

        targetState = targetStateArg.next();

        if (targetState == 0) {
            throw new IllegalArgumentException("Dead state (0) set as domination target. Use CheckForExtinction instead.");
        }
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen in this process.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState stepState) throws HaltCondition {
        if (targetState == -1) {
            checkAllStates(stepState);
        } else {
            doCheck(targetState, stepState);
        }
    }

    private void checkAllStates(StepState stepState) throws HaltCondition {
        Integer[] states = layer.getViewer().getStateMapViewer().getStates();

        for (Integer targetState : states) {
            // The dead state cannot "dominate" the system (that's extinction)
            if (targetState == 0) {
                continue;
            }
            doCheck(targetState, stepState);
        }

    }

    private void doCheck(int target, StepState stepState) throws HaltCondition {
        double numTargetCells = layer.getViewer().getStateMapViewer().getCount(target);
        double numCells = layer.getViewer().getOccupiedSites().size();

        double fraction = numTargetCells / numCells;

        if (fraction >= targetFraction) {
            throw new DominationEvent(target);
        }

    }
}
