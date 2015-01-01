/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dbborens on 12/12/14.
 */
public class ContinuumLayerScheduler {

    private boolean isHeld;
    private ScheduledOperations scheduledOperations;
    private ContinuumAgentManager agentManager;
    private ContinuumSolver solver;

    public ContinuumLayerScheduler(ScheduledOperations scheduledOperations, AgentToOperatorHelper agentHelper, ContinuumSolver solver, String id) {
        this.scheduledOperations = scheduledOperations;
        this.solver = solver;
        agentManager = buildAgentManager(agentHelper, id);
        isHeld = false;
    }

    private ContinuumAgentManager buildAgentManager(AgentToOperatorHelper agentHelper, String id) {
        Consumer<DenseVector> injector = vector -> scheduledOperations.inject(vector);
        Consumer<DenseMatrix> exponentiator = matrix -> scheduledOperations.apply(matrix);
        ContinuumAgentScheduler agentScheduler = new ContinuumAgentScheduler(injector, exponentiator, agentHelper);
        ContinuumAgentManager ret = new ContinuumAgentManager(agentScheduler, id);
        return ret;
    }

    public void hold() {
        if (isHeld) {
            throw new IllegalStateException("Attempted to initiate hold for already held continuum layer.");
        }

        isHeld = true;
    }

    public void release() {
        if (!isHeld) { throw new IllegalStateException("Attempted to release hold on a continuum layer that was not already held."); }
        isHeld = false;

        solve();
    }

    public void apply(Matrix matrix) {
        scheduledOperations.apply(matrix);
        solveIfNotHeld();
    }

    private void solveIfNotHeld() {
        if (isHeld)
            return;

        solve();
    }

    private void solve() {
        agentManager.apply();
        solver.solve();
    }
    public void inject(Coordinate target, double delta) {
        scheduledOperations.inject(target, delta);
        solveIfNotHeld();
    }

    public void exp(Coordinate target, double b) {
        scheduledOperations.exp(target, b);
        solveIfNotHeld();
    }

    public void reset() {
        isHeld = false;
        scheduledOperations.reset();
    }

    public ContinuumAgentLinker getLinker(Function<Coordinate, Double> stateLookup) {
        return agentManager.getLinker(stateLookup);
    }
}
