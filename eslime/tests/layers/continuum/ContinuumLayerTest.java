/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;//import junit.framework.TestCase;

import no.uib.cipr.matrix.DenseVector;
import test.EslimeTestCase;

public class ContinuumLayerTest extends EslimeTestCase {

    private MockContinuumLayerContent content;
    private MockContinuumLayerScheduler scheduler;
    private ContinuumLayer query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        content = new MockContinuumLayerContent();
        scheduler = new MockContinuumLayerScheduler();
        query = new ContinuumLayer(scheduler, content);
    }

    public void testGetId() throws Exception {
        assertEquals("test", query.getId());
    }

    public void testReset() throws Exception {
        query.reset();
        assertTrue(content.isReset());
        assertTrue(scheduler.isReset());
    }

    public void testGetState() throws Exception {
        double[] expected = new double[] {1.0, 2.0, 3.0};
        content.setState(new DenseVector(expected));

        assertArraysEqual(expected, query.getState(), false);
    }

    public void testGetScheduler() throws Exception {
        assertTrue(scheduler == query.getScheduler());
    }
}