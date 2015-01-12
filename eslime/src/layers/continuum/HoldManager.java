/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;

import java.util.function.Function;

/**
 * Created by dbborens on 1/9/15.
 */
public class HoldManager {

    private ContinuumAgentManager manager;
    private ContinuumSolver solver;
    private boolean held;

    public HoldManager(ContinuumAgentManager manager, ContinuumSolver solver) {
        this.manager = manager;
        this.solver = solver;
        this.held = false;
    }

    public void hold() {
        if (held) {
            throw new IllegalStateException("Attempted to initiate hold for already held continuum layer.");
        }

        held = true;
    }

    public void release() {
        if (!held) {
            throw new IllegalStateException("Attempted to release hold on a continuum layer that was not already held.");
        }

        held = false;

        solve();
    }

    private void solveIfNotHeld() {
        if (held)
            return;

        solve();
    }

    public void resolve(Runnable runnable) {
        runnable.run();
        solveIfNotHeld();
    }

    public void solve() {
        if (held) {
            throw new IllegalStateException("Attempting to solve while hold is in place.");
        }

        manager.apply();
        solver.solve();
    }

    public void reset() {
        manager.reset();
        held = false;
    }

    public ContinuumAgentLinker getLinker(Function<Coordinate, Double> stateLookup) {
        return manager.getLinker(stateLookup);
    }

    public String getId() {
        return manager.getId();
    }

    public boolean isHeld() {
        return held;
    }
}
