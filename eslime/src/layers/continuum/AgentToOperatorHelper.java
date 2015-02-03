/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public DenseMatrix getOperator(List<RelationshipTuple> relationships) {
        DenseMatrix matrix = new DenseMatrix(n, n);
        BiConsumer<Integer, Double> consumer = (i, v) -> matrix.add(i, i, v);
        Function<RelationshipTuple, Double> expLookup = RelationshipTuple::getExp;
        apply(relationships, expLookup, consumer);
        return matrix;
    }

    public DenseVector getSource(List<RelationshipTuple> relationships) {
        DenseVector vector = new DenseVector(n);
        BiConsumer<Integer, Double> consumer = (i, v) -> vector.add(i, v);
        Function<RelationshipTuple, Double> injLookup = RelationshipTuple::getInj;
        apply(relationships, injLookup, consumer);
        return vector;
    }

    private void apply(List<RelationshipTuple> relationships, Function<RelationshipTuple, Double> lookup, BiConsumer<Integer, Double> consumer) {
        relationships.forEach(relationship -> {
            Coordinate c = relationship.getCoordinate();
            Double v = lookup.apply(relationship);
            Integer i = indexer.apply(c);
            consumer.accept(i, v);
        });
    }
}
