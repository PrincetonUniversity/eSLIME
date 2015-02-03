/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import factory.cell.Reaction;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AgentToOperatorHelperTest extends LinearMocks {

    private AgentToOperatorHelper query;
    private List<RelationshipTuple> list;
    private Reaction reaction;

    @Before
    public void init() throws Exception {
        reaction = new Reaction(1.0, 2.0, "test");
        RelationshipTuple relationship = new RelationshipTuple(a, reaction);
        list = Stream.of(relationship).collect(Collectors.toList());
        query = new AgentToOperatorHelper(indexer, 3);
    }

    @Test
    public void getSource() {
        DenseVector expected = vector(1.0, 0.0, 0.0);
        DenseVector actual = query.getSource(list);
        assertVectorsEqual(expected, actual, epsilon);
    }

    @Test
    public void getOperator() {
        DenseMatrix expected = matrix(2.0, 0.0, 0.0);
        DenseMatrix actual = query.getOperator(list);
        assertMatricesEqual(expected, actual, epsilon);
    }

}