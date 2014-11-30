/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.arguments.CellDescriptor;
import control.halt.HaltCondition;
import control.halt.LatticeFullEvent;
import control.identifiers.Coordinate;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;

public class Scatter extends CellProcess {

    private HashSet<Coordinate> candidates;
    private CellDescriptor cellDescriptor;

    public Scatter(BaseProcessArguments arguments, CellProcessArguments cpArguments, CellDescriptor cellDescriptor) {
        super(arguments, cpArguments);
        this.cellDescriptor = cellDescriptor;
    }

    @Override
    public void init() {
        candidates = null;
    }

    public void target(GillespieState gs) throws HaltCondition {
        // Construct initial set of candidates
        candidates = new HashSet<>();

        for (Coordinate c : activeSites) {
            if (!layer.getViewer().isOccupied(c)) {
                candidates.add(c);
            }
        }
        if (gs != null) {
            gs.add(this.getID(), candidates.size(), candidates.size() * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        System.out.println("Executing Scatter.");
        if (candidates == null) {
            throw new IllegalStateException("fire() invoked on scatter before target().");
        }

        int n = maxTargets.next();
        for (int i = 0; i < n; i++) {
            if (candidates.isEmpty()) {
                throw new LatticeFullEvent();
            }

            // Choose target randomly
            Coordinate[] cVec = candidates.toArray(new Coordinate[0]);

            int o = p.getRandom().nextInt(cVec.length);
            Coordinate target = cVec[o];

            Cell cell = cellDescriptor.next();

            //System.out.println("   Placing cell of type " + cell.getState() + " at location " + target);
            layer.getUpdateManager().place(cell, target);

            //state.highlight(target);
            candidates.remove(target);
        }

        // Make sure that a new target must be chosen prior to next invocation.
        candidates = null;

    }
}
