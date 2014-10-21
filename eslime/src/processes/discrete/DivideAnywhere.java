/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;

public class DivideAnywhere extends BulkDivisionProcess {

    private Coordinate[] candidates = null;

    public DivideAnywhere(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                          GeneralParameters p, Argument<Integer> maxTargets) {
        super(loader, layerManager, activeSites, id, p, maxTargets);
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
