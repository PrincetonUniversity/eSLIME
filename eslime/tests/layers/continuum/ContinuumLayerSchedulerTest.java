/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;//import junit.framework.TestCase;

import control.identifiers.Coordinate;
import geometry.Geometry;
import structural.utilities.MatrixUtils;
import test.EslimeTestCase;

public class ContinuumLayerSchedulerTest extends EslimeTestCase {

    private ContinuumLayerScheduler query;
    private MockScheduledOperations so;
    private MockContinuumSolver solver;
    private Geometry geom;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geom = makeLinearGeometry(3);
        so = new MockScheduledOperations(geom);
        solver = new MockContinuumSolver();
        //query = new ContinuumLayerScheduler(so, solver);
    }

    public void testHoldRelease() {
        // Activate a hold.
        query.hold();

        // Schedule an event--the solver should not be triggered.
        query.inject(new Coordinate(0, 0, 0), 1.0);
        assertFalse(solver.isSolved());

        // Release hold.
        query.release();

        // The solver should have been triggered.
        assertTrue(solver.isSolved());
    }

    public void testApply() {
        assertFalse(so.isApplied());
        query.apply(MatrixUtils.I(3));
        assertTrue(so.isApplied());
    }

    public void testScalarInject() {
        assertFalse(so.isInjected());
        query.inject(new Coordinate(0, 0, 0), 1.0);
        assertTrue(so.isInjected());
    }

    public void testVectorInject() {
        fail("Not yet implemented");
    }

    public void testExp() {
        assertFalse(so.isScaled());
        query.exp(new Coordinate(0, 0, 0), 1.0);
        assertTrue(so.isScaled());
    }
}