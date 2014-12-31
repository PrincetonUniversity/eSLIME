/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentScheduler {

    private Consumer<DenseVector> injector;
    private Consumer<DenseMatrix> exponentiator;
    private AgentToOperatorHelper helper;

    public ContinuumAgentScheduler(Consumer<DenseVector> injector, Consumer<DenseMatrix> exponentiator, AgentToOperatorHelper helper) {
        this.injector = injector;
        this.exponentiator = exponentiator;
        this.helper = helper;
    }

    public void inject(Collection<RelationshipTuple> relationships) {
        DenseVector delta = helper.asVector(relationships);
        injector.accept(delta);
    }

    public void exponentiate(Collection<RelationshipTuple> relationshipTuples) {
        DenseMatrix exponents = helper.asMatrix(relationshipTuples);
        exponentiator.accept(exponents);
    }
}
