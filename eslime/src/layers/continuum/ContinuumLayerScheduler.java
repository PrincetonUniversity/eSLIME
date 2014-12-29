/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.Matrix;

/**
 * Created by dbborens on 12/12/14.
 */
public class ContinuumLayerScheduler {

    private boolean isHeld;
    private ScheduledOperations scheduledOperations;
    private ContinuumSolver solver;

    public ContinuumLayerScheduler(ScheduledOperations scheduledOperations, ContinuumSolver solver) {
        this.scheduledOperations = scheduledOperations;
        this.solver = solver;

        isHeld = false;
    }

    public void hold() {
        if (isHeld) {
            throw new IllegalStateException("Attempted to initiate hold for already held continuum layer.");
        }

        isHeld = true;
    }

    public void release() {
        if (!isHeld) {
            throw new IllegalStateException("Attempted to release hold on a continuum layer that was not already held.");
        }
        isHeld = false;

        solver.solve();
    }

    public void apply(Matrix matrix) {
        scheduledOperations.apply(matrix);
        solveIfNotHeld();
    }

    private void solveIfNotHeld() {
        if (isHeld)
            return;

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
}
