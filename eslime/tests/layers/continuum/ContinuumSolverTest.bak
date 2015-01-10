/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.utilities.MatrixUtils;
import test.EslimeTestCase;

public class ContinuumSolverTest extends EslimeTestCase {

    private MockContinuumLayerContent content;
    private MockScheduledOperations so;
    private MockSteadyStateHelper helper;

    private ExposedContinuumSolver query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Geometry geom = makeLinearGeometry(3);
//        content = new MockContinuumLayerContent();
        so = new MockScheduledOperations(geom);
        helper = new MockSteadyStateHelper();

        query = new ExposedContinuumSolver(content, so);
        query.setHelper(helper);
    }

    public void testSolve() throws Exception {
        Vector source = new DenseVector(3);
        source.set(1, 3.0);

        Matrix operator = MatrixUtils.I(3);

        Vector initial = new DenseVector(3);
        initial.set(0, 0.5);

        so.setOperator(operator);
        so.setSource(source);
        content.setState(initial);
        content.setTriggered(false);

        query.solve();

        assertTrue(helper.isSolved());
        assertTrue(content.isTriggered());
    }

    /**
     * Exposes helper object so that it can be overriden with a mock
     */
    private class ExposedContinuumSolver extends ContinuumSolver {

        public ExposedContinuumSolver(ContinuumLayerContent content, ScheduledOperations so) {
            super(content, so);
        }

        public void setHelper(SteadyStateHelper helper) {
            this.helper = helper;
        }
    }
}