/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;

public class Divide extends BulkDivisionProcess {

    private Coordinate[] candidates;

    public Divide(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments, cpArguments);
    }


    @Override
    public void init() {
        candidates = null;
    }

    public void target(GillespieState gs) throws HaltCondition {
        HashSet<Coordinate> candSet = layer.getViewer().getDivisibleSites();
        candidates = candSet.toArray(new Coordinate[0]);
        if (gs != null) {
            gs.add(getID(), candidates.length, candidates.length * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        execute(candidates);
        candidates = null;
    }

}
