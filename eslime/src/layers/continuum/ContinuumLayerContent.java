/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import java.util.function.Function;

/**
 * Created by dbborens on 12/26/14.
 */
public class ContinuumLayerContent {
    protected Vector state;

    // Map of coordinate --> vector index
    private Function<Coordinate, Integer> indexer;
    private int n;

    public ContinuumLayerContent(Function<Coordinate, Integer> indexer, int n) {
        this.indexer = indexer;
        this.n = n;
    }

    public double get(Coordinate c) {
        int index = indexer.apply(c);
        double ret = state.get(index);
        return ret;
    }

    public Vector getState() {
        return state;
    }

    public void setState(Vector state) {
        this.state = state;
    }

    public void reset() {
        this.state = new DenseVector(n);
    }

}
