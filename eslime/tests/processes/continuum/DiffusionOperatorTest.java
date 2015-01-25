/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import org.junit.Before;
import org.junit.Test;
import structural.utilities.MatrixUtils;
import test.LinearMocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DiffusionOperatorTest extends LinearMocks {

    private DiffusionConstantHelper helper;
    private DiffusionOperator query;

    @Before
    public void init() throws Exception {
        helper = mock(DiffusionConstantHelper.class);
        when(helper.getDiagonalValue()).thenReturn(-0.2);
        when(helper.getNeighborValue()).thenReturn(0.1);
        query = new DiffusionOperator(helper, geom);
    }

    /**
     * The operator has the neighbor value in the neighbor
     * positions, the diagonal value in along the diagonal,
     * and it respects all properties of the geometry (size,
     * neighbors).
     */
    @Test
    public void contentsAsExpected() throws Exception {
        DenseMatrix expected = makeMatrix();
        assertMatricesEqual(expected, query, epsilon);
    }

    private DenseMatrix makeMatrix() {
        DenseMatrix ret = new DenseMatrix(3, 3);

        ret.add(0, 0, -0.2);
        ret.add(0, 1, 0.1);

        ret.add(1, 0, 0.1);
        ret.add(1, 1, -0.2);
        ret.add(1, 2, 0.1);

        ret.add(2, 1, 0.1);
        ret.add(2, 2, -0.2);

        return ret;
    }
}