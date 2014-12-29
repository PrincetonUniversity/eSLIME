/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.utilities.MatrixUtils;
import test.EslimeTestCase;

public class ScheduledOperationsTest extends EslimeTestCase {

    private ScheduledOperations query;
    private Geometry geom;

    private Coordinate a, b;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        geom = makeLinearGeometry(3);
        a = geom.getCanonicalSites()[0];
        b = geom.getCanonicalSites()[1];

        query = new ScheduledOperations(geom);
    }

    public void testInject() throws Exception {
        query.inject(a, 1.0);
        query.inject(a, 0.5);
        query.inject(b, 2.0);

        Vector expectedSource = new DenseVector(new double[] {1.5, 2.0, 0});
        Vector actualSource = query.getSource();

        Matrix expectedOperator = MatrixUtils.I(3);
        Matrix actualOperator = query.getOperator();

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));
    }

    public void testScale() throws Exception {
        query.exp(a, 3.0);
        query.exp(a, 0.5);
        query.exp(b, 2.0);

        Vector expectedSource = new DenseVector(3);
        Vector actualSource = query.getSource();

        Matrix expectedOperator = new DenseMatrix(3, 3);
        expectedOperator.set(0, 0, 4.5);
        expectedOperator.set(1, 1, 3.0);
        expectedOperator.set(2, 2, 1.0);

        Matrix actualOperator = query.getOperator();

        System.out.println("Expected");
        System.out.println(MatrixUtils.matrixForm(expectedOperator));

        System.out.println("Actual");
        System.out.println(MatrixUtils.matrixForm(actualOperator));

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));
    }

    public void testApply() throws Exception {
        Matrix toApply = new DenseMatrix(3, 3);
        toApply.set(1, 1, 1.0);
        query.apply(toApply);

        Matrix expectedOperator = MatrixUtils.I(3);
        expectedOperator.add(1, 1, 1.0);

        Matrix actualOperator = query.getOperator();

        Vector expectedSource = new DenseVector(3);
        Vector actualSource = query.getSource();

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));
    }

    public void testReset() throws Exception {
        Matrix expectedOperator = MatrixUtils.I(3);
        Matrix actualOperator = query.getOperator();

        Vector expectedSource = new DenseVector(3);
        Vector actualSource = query.getSource();

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));

        // Change the source vector
        query.inject(a, 1.0);
        expectedSource.set(0, 1.0);

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));

        // Apply a new matrix
        Matrix toApply = new DenseMatrix(3, 3);
        toApply.set(1, 1, 1.0);
        query.apply(toApply);

        expectedOperator = MatrixUtils.I(3);
        expectedOperator.add(1, 1, 1.0);

        actualOperator = query.getOperator();

        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));
        assertVectorsEqual(expectedSource, actualSource, epsilon);

        // Reset
        query.reset();

        expectedOperator = MatrixUtils.I(3);
        actualOperator = query.getOperator();

        expectedSource = new DenseVector(3);
        actualSource = query.getSource();

        assertVectorsEqual(expectedSource, actualSource, epsilon);
        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));

        // Apply identity matrix

        toApply = MatrixUtils.I(3);
        query.apply(toApply);

        expectedOperator = MatrixUtils.I(3);
        expectedOperator.scale(2.0);

        actualOperator = query.getOperator();

        assertTrue(MatrixUtils.equal(expectedOperator, actualOperator));
    }
}