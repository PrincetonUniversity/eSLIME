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

public class ActiveLayerDivide extends BulkDivisionProcess {

    int depth;
    private Coordinate[] candidates = null;

    public ActiveLayerDivide(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites,
                             int id,
                             GeneralParameters p, Argument<Integer> maxTargets) {
        super(loader, layerManager, activeSites, id, p, maxTargets);

        depth = Integer.valueOf(get("depth"));
    }

    public void target(GillespieState gs) throws HaltCondition {

        // Choose a random active cell.
        HashSet<Coordinate> superset = layer.getViewer().getDivisibleSites();
        HashSet<Coordinate> candSet = new HashSet<>(superset.size());
        for (Coordinate c : superset) {
            // Look for vacancies within the active layer depth
            Coordinate[] vacancies = layer.getLookupManager().getNearestVacancies(c, depth);

            // It's a division candidate iff it has vacant neighbors within the
            // active layer depth (i.e., is in the active layer).
            if (vacancies.length > 0) {
                candSet.add(c);
            }
        }
        candidates = candSet.toArray(new Coordinate[0]);

        if (gs != null) {
            gs.add(getID(), candidates.length, candidates.length * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {


        execute(candidates);
    }

}
