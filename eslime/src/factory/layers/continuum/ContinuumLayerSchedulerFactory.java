/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.continuum;

import control.identifiers.Coordinate;
import layers.continuum.*;
import layers.continuum.solve.SteadyState;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dbborens on 1/8/15.
 */
public abstract class ContinuumLayerSchedulerFactory {

    public static ContinuumLayerScheduler instantiate(ContinuumLayerContent content, Function<Coordinate, Integer> indexer, int n, String id) {
        ScheduledOperations so = new ScheduledOperations(indexer, n);
        AgentToOperatorHelper helper = new AgentToOperatorHelper(indexer, n);
        ContinuumAgentManager agentManager = buildAgentManager(helper, so, id);
        SteadyState steadyState = new SteadyState();
        ContinuumSolver solver = new ContinuumSolver(content, so, steadyState);
        HoldManager holdManager = new HoldManager(agentManager, solver);
        return new ContinuumLayerScheduler(so, holdManager);
    }

    private static ContinuumAgentManager buildAgentManager(AgentToOperatorHelper agentHelper, ScheduledOperations so, String id) {
        Consumer<DenseVector> injector = vector -> so.inject(vector);
        Consumer<DenseMatrix> exponentiator = matrix -> so.apply(matrix);
        ReactionLoader agentScheduler = new ReactionLoader(injector, exponentiator, agentHelper);
        ContinuumAgentIndex injIndex = new ContinuumAgentIndex(cell -> cell.getLinker().getInj(id));
        ContinuumAgentIndex expIndex = new ContinuumAgentIndex(cell -> cell.getLinker().getExp(id));
        ContinuumAgentManager ret = new ContinuumAgentManager(agentScheduler, injIndex, expIndex, id);
        return ret;
    }

}
