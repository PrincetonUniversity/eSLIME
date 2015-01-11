/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import java.util.stream.Stream;

public class AgentToOperatorHelperTest extends LinearMocks {

    private AgentToOperatorHelper query;
    private Stream<RelationshipTuple> stream;

    @Before
    public void init() throws Exception {
        RelationshipTuple relationship = new RelationshipTuple(a, 1.0);
        stream = Stream.of(relationship);
        query = new AgentToOperatorHelper(indexer, 3);
    }

    @Test
    public void asMatrix() {
        DenseMatrix expected = matrix(1.0, 0.0, 0.0);
        DenseMatrix actual = query.asMatrix(stream);
        assertMatricesEqual(expected, actual, epsilon);
    }

    @Test
    public void asVector() {
        DenseVector expected = vector(1.0, 0.0, 0.0);
        DenseVector actual = query.asVector(stream);
        assertVectorsEqual(expected, actual, epsilon);
    }
}