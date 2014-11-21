/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import control.arguments.ConstantDouble;
import control.halt.HaltCondition;
import control.halt.ManualHaltEvent;
import control.halt.StepMaxReachedEvent;
import io.serialize.MockSerializationManager;
import io.serialize.SerializationManager;
import layers.MockLayerManager;
import processes.Process;
import processes.discrete.ManualHalt;
import processes.temporal.Tick;
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
        Process[] processes = new Process[0];
        mgr.setTriggeredProcesses(processes);
        // Each of the following should have been called 5 times:
        HaltCondition halt = integrator.go();

        assertTrue(halt instanceof StepMaxReachedEvent);
        assertEquals(5, mgr.getTimesIterated());
    }

    public void testTimeAppliedAtHaltNoAdvance() throws Exception {
        p.setT(10000);
        MockLayerManager lm = new MockLayerManager();
        Process[] processes = new Process[] {
                new ManualHalt(null, lm, null, 0, null, null)
        };
        mgr.setTriggeredProcesses(processes);
        ExposedIntegrator query = new ExposedIntegrator(p, mgr, sm);
        query.setTime(3.0);

        HaltCondition halt = query.go();
        assertTrue(halt instanceof ManualHaltEvent);
        assertEquals(3.0, halt.getGillespie(), epsilon);
    }

    public void testTimeAppliedAtHaltAfterClockAdvance() throws Exception {
        p.setT(10000);
        MockLayerManager lm = new MockLayerManager();
        Process[] processes = new Process[] {
                new Tick(null, null, 0, null, new ConstantDouble(1.0)),
                new ManualHalt(null, lm, null, 0, null, null)
        };
        mgr.setTriggeredProcesses(processes);
        ExposedIntegrator query = new ExposedIntegrator(p, mgr, sm);
        query.setTime(3.0);

        HaltCondition halt = query.go();
        assertTrue(halt instanceof ManualHaltEvent);
        assertEquals(4.0, halt.getGillespie(), epsilon);
    }
    private class ExposedIntegrator extends Integrator {

        public ExposedIntegrator(GeneralParameters p, ProcessManager processManager, SerializationManager serializationManager) {
            super(p, processManager, serializationManager);
        }

        public void setTime(double time) {
            this.time = time;
        }
    }

    public void testTimeAppliedAtMaxStep() throws Exception {
        p.setT(5);
        mgr.setStepStateDt(2.0);
        mgr.setTriggeredProcesses(new Process[0]);
        ExposedIntegrator query = new ExposedIntegrator(p, mgr, sm);
        HaltCondition ret = query.go();
        assertTrue(ret instanceof StepMaxReachedEvent);
        assertEquals(10.0, ret.getGillespie(), epsilon);
    }
}
