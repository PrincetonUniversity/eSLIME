/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import io.factory.MockProcessFactory;
import junit.framework.TestCase;
import layers.MockLayerManager;
import processes.MockProcess;
import processes.Process;
import processes.StepState;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class ProcessManagerTest extends TestCase {

    private static final int CURRENT_N = 2;
    private static final double CURRENT_TIME = 1.7;
    MockProcess yes, no;
    ProcessManager query;
    MockProcessFactory factory;
    MockLayerManager layerManager;

    @Override
    protected void setUp() throws Exception {
        initHelperObjects();
        initYesAndNo();
        query = new ProcessManager(factory, layerManager);
    }

    private void initHelperObjects() {
        layerManager = new MockLayerManager();
        factory = new MockProcessFactory();
    }

    /**
     * Construct two mock processes, only one of which should be triggered.
     */
    private void initYesAndNo() {

        yes = new MockProcess();
        yes.setLayerManager(layerManager);
        // These processes each run once, but only one runs at the current time.
        yes.setStart(CURRENT_N);
        yes.setPeriod(0);

        no = new MockProcess();
        no.setLayerManager(layerManager);
        no.setStart(CURRENT_N + 1);
        no.setPeriod(0);

        Process[] processes = new Process[]{
                yes,
                no
        };

        factory.setProcesses(processes);
    }

    public void testGetTriggeredProcesses() throws Exception {
        // Call getTriggeredProcesses.
        Process[] triggeredProcesses = query.getTriggeredProcesses(CURRENT_N);

        assertEquals(1, triggeredProcesses.length);
        assertEquals(yes, triggeredProcesses[0]);
    }

    public void testTriggered() throws Exception {
        // Case 1a: a 1-time event that has not yet been triggered.
        assertFalse(triggerTest(3, 0));

        // Case 1b: a 1 time event that has alrady been triggered.
        assertFalse(triggerTest(1, 0));

        // Case 2: a 1-time event that is currently triggered.
        assertTrue(triggerTest(2, 0));

        // Case 3: an ongoing event that has not yet started.
        assertFalse(triggerTest(3, 1));

        // Case 4a: an ongoing event that has started, and should fire now.
        assertTrue(triggerTest(1, 1));

        // Case 4b: an ongoing event that has started, but is out of phase.
        assertFalse(triggerTest(1, 2));
    }

    public boolean triggerTest(int start, int period) {
        // Construct a mock process.
        MockProcess process = new MockProcess();

        // Give it the specified start and period arguments.
        process.setPeriod(period);
        process.setStart(start);

        // Run ProcessManager::triggered().
        return query.triggered(CURRENT_N, process);
    }

    public void testDoTriggeredProcesses() throws Exception {
        // Execute doTriggeredProcesses.
        query.doTriggeredProcesses(new StepState(CURRENT_TIME, CURRENT_N));

        // Verify that only the triggered process actually took place.
        assertEquals(0, no.getTimesFired());
        assertEquals(1, yes.getTimesFired());
    }

    public void testStepStateRenewal() throws Exception {
        StepState first = query.doTriggeredProcesses(new StepState(0.0, 0));
        StepState second = query.doTriggeredProcesses(new StepState(0.0, 0));

        // First and second should be distinct objects (reference inequality)
        assertFalse(first == second);
    }
}
