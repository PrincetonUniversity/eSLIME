/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;


import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReactionLoaderTest extends LinearMocks {

    private Vector capturedVector;
    private Matrix capturedMatrix;

    private AgentToOperatorHelper helper;
    private Stream<RelationshipTuple> stream;

    private ReactionLoader query;

    @Before
    public void init() {
        // For some reason, I can't set up a captor on a consumer, so I am doing this.
        Consumer<DenseVector> vectorCaptor = this::captureVector;
        Consumer<DenseMatrix> matrixCaptor = this::captureMatrix;

        helper = mock(AgentToOperatorHelper.class);
        stream = (Stream<RelationshipTuple>) mock(Stream.class);

        query = new ReactionLoader(vectorCaptor, matrixCaptor, helper);
    }

    @Test
    public void testInject() throws Exception {
        DenseVector vector = vector(1.0, 2.0, 3.0);
        when(helper.getSource(any())).thenReturn(vector);
        query.inject(stream);
        assertEquals(vector, capturedVector);
    }

    @Test
    public void testExponentiate() throws Exception {
        DenseMatrix matrix = matrix(1.0, 2.0, 3.0);
        when(helper.getOperator(any())).thenReturn(matrix);
        query.exponentiate(stream);
        assertEquals(matrix, capturedMatrix);
    }

    private void captureVector(Vector vector) {
        capturedVector = vector;
    }

    private void captureMatrix(Matrix matrix) {
        capturedMatrix = matrix;
    }
}