/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import org.junit.Before;
import org.junit.Test;
import test.TestBase;

import static org.junit.Assert.*;

public class DiffusionConstantHelperTest extends TestBase {

    private int dimensionality;
    private int connectivity;
    private double baseConstant;
    private DiffusionConstantHelper query;

    @Before
    public void init() throws Exception {
        // i.e., a triangular 2D lattice
        dimensionality = 2;
        connectivity = 3;

        baseConstant = 0.1;

        query = new DiffusionConstantHelper(baseConstant, connectivity, dimensionality);
    }

    @Test
    public void neighborAccountsForGeometry() {
        double expected = baseConstant * dimensionality / connectivity;
        assertEquals(expected, query.getNeighborValue(), epsilon);
    }

    /**
     * The origin concentration should be the negative of the sum of amount
     * transferred to neighbors. (Negative because the operator is actually
     * Q - I, where Q is the "true" diffusion matrix--i.e., how much the
     * origin will lose.)
     *
     * There are n*m neighbors, and each gets D*n/m. So the origin should
     * lose (n^2) * D.
     */
    @Test
    public void totalConcentrationConserved() {
        double expected = -1.0 * Math.pow(dimensionality, 2.0) * baseConstant;
        assertEquals(expected, query.getDiagonalValue(), epsilon);
    }

    /**
     * If the origin would lose more than the total of its original
     * concentration, then the input is illegal.
     */
    @Test(expected = IllegalArgumentException.class)
    public void excessiveInputThrows() {
        query = new DiffusionConstantHelper(0.51, connectivity, dimensionality);
    }

    /**
     * Diffusion constants cannot be negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void belowZeroThrows() {
        query = new DiffusionConstantHelper(-1.0, connectivity, dimensionality);
    }
}