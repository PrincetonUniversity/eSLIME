/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 * Created by dbborens on 12/28/14.
 */
public class MockScheduledOperations extends ScheduledOperations {
    private boolean injected;
    private boolean scaled;
    private boolean applied;

    public MockScheduledOperations(Geometry geom) {
        super(geom::coordToIndex, geom.getCanonicalSites().length);
    }

    @Override
    public void inject(Coordinate coordinate, double delta) {
        injected = true;
    }

    @Override
    public void exp(Coordinate coordinate, double b) {
        scaled = true;
    }

    @Override
    public void apply(Matrix toAdd) {
        applied = true;
    }

    public boolean isInjected() {
        return injected;
    }

    public boolean isScaled() {
        return scaled;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setOperator(Matrix operator) {
        this.operator = operator;
    }

    public void setSource(Vector source) {
        this.source = source;
    }
}
