/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.continuum;

import control.identifiers.Coordinate;
import layers.continuum.*;

import java.util.function.Function;

/**
 * Created by dbborens on 1/8/15.
 */
public abstract class ContinuumLayerSchedulerFactory {

    public static ContinuumLayerScheduler instantiate(ContinuumLayerContent content, Function<Coordinate, Integer> indexer, int n, String id) {
        ScheduledOperations so = new ScheduledOperations(indexer, n);
        AgentToOperatorHelper helper = new AgentToOperatorHelper(indexer, n);
        ContinuumSolver solver = new ContinuumSolver(content, so);
        return new ContinuumLayerScheduler(so, helper, solver, id);
    }
}
