/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package control;

import control.halt.HaltCondition;
import control.halt.StepMaxReachedEvent;
import io.serialize.MockSerializationManager;
import junit.framework.TestCase;
import processes.StepState;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class IntegratorTest extends EslimeTestCase {

    // Items used during construction
    private MockGeneralParameters p;
    private MockSerializationManager sm;
    private MockProcessManager mgr;

    // And now, the thing to be tested...
    private Integrator integrator;

    @Override
    protected void setUp() throws Exception {
        // Initialize infrastructure objects
        p = new MockGeneralParameters();
        sm = new MockSerializationManager();
        mgr = new MockProcessManager();
        integrator = new Integrator(p, mgr, sm);
    }

    public void testGo() throws Exception {
        // Set T to 5 loops.
        p.setT(5);

        // Each of the following should have been called 5 times:
        HaltCondition halt = integrator.go();

        assertTrue(halt instanceof StepMaxReachedEvent);
        assertEquals(5, mgr.getTimesIterated());
    }

//    public void testStateApplied() throws Exception {
//        p.setT(1);
//        mgr.setStepStateDt(1.0);
//        // Each of the following should have been called 5 times:
//        integrator.go();
//        StepState stepState = integrator.getStepState();
//        assertEquals(1.0, stepState.getDt());
//        assertEquals(1.0, stepState.getTime());
//    }
}
