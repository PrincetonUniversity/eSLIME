/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.utilities.MatrixUtils;
import test.EslimeTestCase;

public class SteadyStateHelperTest extends EslimeTestCase {

    private DenseVector initial;
    private SteadyStateHelper query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        query = new SteadyStateHelper();

        initial = new DenseVector(3);
        initial.set(1, 1.0);
    }

    /**
     * Identity matrix; no initial concentration or source.
     * @throws Exception
     */
    public void testTrivialCase() throws Exception {
        Matrix operator = MatrixUtils.I(3);
        DenseVector source = new DenseVector(3);
        initial = new DenseVector(3);               // Zero out initial value
        doTest(source, operator, new DenseVector(3));
    }

    /**
     * In the special case that there are no operations and no sources,
     * the system should return the original vector.
     *
     * @throws Exception
     */
    public void testNoChangeCase() throws Exception {
        Matrix operator = MatrixUtils.I(3);
        DenseVector source = new DenseVector(3);
        doTest(source, operator, initial.copy());
    }

    /**
     * If there is zero initial value and no source, then the solution
     * remains identically zero.
     */
    public void testZeroValueCase() throws Exception {
        Matrix operator = diffusion();
        DenseVector source = new DenseVector(3);
        initial = new DenseVector(3);               // Zero out initial value
        doTest(source, operator, new DenseVector(3));
    }

    /**
     * If a coordinate with a non-zero initial value has a diagonal
     * operator value greater than 1.0, it will diverge--we expect an
     * error.
     */
    public void testDivergentCaseThrows() {
        Matrix operator = new DenseMatrix(3, 3);
        operator.set(1, 1, 2.0);
        DenseVector source = new DenseVector(3);
        testThrows(source, operator, initial);

    }

    /**
     * If the matrix is not the identity, but every row and column sums to one,
     * then there is no steady state solution -- we expect an error.
     */
    public void testNoSteadyStateThrows() {
        Matrix operator = advection();
        DenseVector source = new DenseVector(3);
        testThrows(source, operator, initial);
    }

    /**
     * If there is no source and the matrix is non-identical and has a steady
     * state solution, that steady state solution must be zero.
     */
    public void testDecay() {
        DenseVector source = new DenseVector(3);
        Matrix operator = diffusion();
        DenseVector expected = new DenseVector(3);
        doTest(source, operator, expected);
    }

    /**
     * A source at the middle coordinate and diffusion with absorbing
     * boundaries; should be solved as normal and have a non-trivial
     * solution.
     *
     * @throws Exception
     */
    public void testGeneralCase() throws Exception {
        DenseVector source = new DenseVector(3);
        source.set(1, 1);
        Matrix operator = diffusion();
        DenseVector expected = new DenseVector(new double[] {5.0, 10.0, 5.0});
        doTest(source, operator, expected);
    }

    private void doTest(DenseVector source, Matrix operator, DenseVector expected) {
        Vector actual = query.solve(source, operator, initial.copy());
        assertVectorsEqual(expected, actual, 1e-14);
    }

    private void testThrows(Vector source, Matrix operator, Vector localInitial) {
        boolean thrown = false;

        try {
            query.solve(source, operator, localInitial);
        } catch (IllegalStateException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    private Matrix diffusion() {

        Matrix operator = new DenseMatrix(3, 3);

        /* Operator:
         *
         * 0.8 0.1 0.0
         * 0.1 0.8 0.1
         * 0.0 0.1 0.8
         */

        operator.set(0, 0, 0.8);
        operator.set(0, 1, 0.1);
        operator.set(0, 2, 0.0);

        operator.set(1, 0, 0.1);
        operator.set(1, 1, 0.8);
        operator.set(1, 2, 0.1);

        operator.set(2, 0, 0.0);
        operator.set(2, 1, 0.1);
        operator.set(2, 2, 0.8);

        return operator;
    }

    /**
     * Advection with periodic boundary conditions. No steady
     * state.
     *
     * @return
     */
    private Matrix advection() {
        Matrix operator = new DenseMatrix(3, 3);

        /* Operator:
         *
         * 0.9 0.1 0.0
         * 0.0 0.9 0.1
         * 0.1 0.0 0.9
         */

        operator.set(0, 0, 0.9);
        operator.set(0, 1, 0.1);
        operator.set(0, 2, 0.0);

        operator.set(1, 0, 0.0);
        operator.set(1, 1, 0.9);
        operator.set(1, 2, 0.1);

        operator.set(2, 0, 0.1);
        operator.set(2, 1, 0.0);
        operator.set(2, 2, 0.9);

        return operator;
    }
}