/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.utilities.MatrixUtils;

import java.util.function.Function;

/**
 * Helper class for continuum layers. Knows what transformations
 * have been scheduled since the last update.
 * Created by dbborens on 12/11/14.
 */
public class ScheduledOperations {

    protected final Matrix identity;
    protected final DenseVector zeroVector;
    protected Matrix operator;
    protected Vector source;
    protected Function<Coordinate, Integer> indexer;

    public ScheduledOperations(Function<Coordinate, Integer> indexer, int n) {
        this.indexer = indexer;

        Matrix bandIdentity = MatrixUtils.I(n);
        identity = new DenseMatrix(bandIdentity);
        zeroVector = new DenseVector(n);

        reset();
    }

    /**
     * Add specified amount to a particular coordinate.
     *
     * @param coordinate
     * @param delta
     */
    public void inject(Coordinate coordinate, double delta) {
        int index = indexer.apply(coordinate);
        double current = source.get(index);
        double next = current + delta;

        source.set(index, next);
    }

    /**
     * Exponentiate a particular location (i.e., add b to the diagonal.)
     *
     * @param coordinate
     * @param b
     */
    public void exp(Coordinate coordinate, double b) {
        int index = indexer.apply(coordinate);
        double current = operator.get(index, index);
        double next = current + b;
        operator.set(index, index, next);
    }

    /**
     * Initialize source and operator vectors. The default values would
     * leave the current state of the field unaltered if applied.
     */
    public void reset() {
        // Reset operator to identity
        operator = identity.copy();

        // Replace source vector with zero vector
        source = zeroVector.copy();
    }

    /**
     * If this is the first matrix operation being scheduled, REPLACE the
     * default (identity) matrix with this matrix. Otherwise, ADD this matrix
     * to the current matrix. Note that this means successive scalings will
     * be additive in magnitude, not multiplicative.
     *
     * @param toApply The matrix to be applied
     */
    public void apply(Matrix toApply) {
        operator.add(toApply);
    }


    public Vector getSource() {
        return source;
    }

    public Matrix getOperator() {
        return operator;
    }

    public void inject(DenseVector delta) {
        source.add(delta);
    }
}
