/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;

import java.util.function.Function;

/**
 * Created by dbborens on 12/12/14.
 */
public class ContinuumLayerScheduler {

    private ScheduledOperations scheduledOperations;
    private HoldManager holdManager;

    public ContinuumLayerScheduler(ScheduledOperations scheduledOperations, HoldManager holdManager) {
        this.scheduledOperations = scheduledOperations;
        this.holdManager = holdManager;
    }

    public void apply(Matrix matrix) {
        holdManager.resolve(() -> scheduledOperations.apply(matrix));
    }

    public void inject(DenseVector vector) {
        holdManager.resolve(() -> scheduledOperations.inject(vector));
    }

    public void inject(Coordinate target, double delta) {
        holdManager.resolve(() -> scheduledOperations.inject(target, delta));
    }

    public void exp(Coordinate target, double b) {
        holdManager.resolve(() -> scheduledOperations.exp(target, b));
    }

    public void reset() {
        holdManager.reset();
        scheduledOperations.reset();
    }

    public ContinuumAgentLinker getLinker(Function<Coordinate, Double> stateLookup) {
        return holdManager.getLinker(stateLookup);
    }

    public String getId() {
        return holdManager.getId();
    }

    public void solve() {
        holdManager.solve();
    }
}
