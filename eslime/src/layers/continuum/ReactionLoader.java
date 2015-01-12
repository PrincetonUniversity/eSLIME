/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by dbborens on 12/31/14.
 */
public class ReactionLoader {

    private Consumer<DenseVector> injector;
    private Consumer<DenseMatrix> exponentiator;
    private AgentToOperatorHelper helper;

    public ReactionLoader(Consumer<DenseVector> injector, Consumer<DenseMatrix> exponentiator, AgentToOperatorHelper helper) {
        this.injector = injector;
        this.exponentiator = exponentiator;
        this.helper = helper;
    }

    public void inject(Stream<RelationshipTuple> relationships) {
        DenseVector delta = helper.getSource(relationships);
        injector.accept(delta);
    }

    public void exponentiate(Stream<RelationshipTuple> relationshipTuples) {
        DenseMatrix exponents = helper.getOperator(relationshipTuples);
        exponentiator.accept(exponents);
    }

    public void apply(Stream<RelationshipTuple> relationships) {
        inject(relationships);
        exponentiate(relationships);
    }
}
