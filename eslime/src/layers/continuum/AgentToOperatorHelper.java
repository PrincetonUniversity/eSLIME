/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Converts injections and exponentiations scheduled
 * by agents to vectors and matrices that can be applied
 * to the schedule.
 *
 * Created by dbborens on 12/31/14.
 */
public class AgentToOperatorHelper {

    // Number of canonical sites
    private int n;

    // Converts coordinates to vector/matrix indices
    private Function<Coordinate, Integer> indexer;

    public AgentToOperatorHelper(Function<Coordinate, Integer> indexer, int n) {
        this.n = n;
        this.indexer = indexer;
    }

    public DenseMatrix asMatrix(Collection<RelationshipTuple> relationships) {
        DenseMatrix matrix = new DenseMatrix(n, n);
        BiConsumer<Integer, Double> consumer = (i, v) -> matrix.add(i, i, v);
        apply(relationships, consumer);
        return matrix;
    }

    public DenseVector asVector(Collection<RelationshipTuple> relationships) {
        DenseVector vector = new DenseVector(n);
        BiConsumer<Integer, Double> consumer = (i, v) -> vector.add(i, v);
        apply(relationships, consumer);
        return vector;
    }

    private void apply(Collection<RelationshipTuple> relationships, BiConsumer<Integer, Double> consumer) {
        for (RelationshipTuple relationship : relationships) {
            Coordinate c = relationship.getCoordinate();
            Double v = relationship.getMagnitude();
            Integer i = indexer.apply(c);
            consumer.accept(i, v);
        }
    }
}
