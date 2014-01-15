package control;

import io.project.MockProcessFactory;
import io.project.ProcessFactory;
import io.project.ProcessLoader;
import junit.framework.TestCase;
import layers.LayerManager;
import processes.Process;
import processes.*;
import structural.GeneralParameters;
import structural.MockGeneralParameters;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class ProcessManagerTest extends TestCase {

    private static final int CURRENT_N = 2;
    MockProcess yes, no;
    ProcessManager query;
    MockProcessFactory factory;
    MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        initHelperObjects();
        initYesAndNo();
        query = new ProcessManager(factory, p);
    }

    private void initHelperObjects() {
        p  = new MockGeneralParameters();
        factory = new MockProcessFactory();
    }

    /**
     * Construct two mock processes, only one of which should be triggered.
     */
    private void initYesAndNo() {

        yes = new MockProcess();

        // These processes each run once, but only one runs at the current time.
        yes.setStart(CURRENT_N);
        yes.setPeriod(0);

        no = new MockProcess();
        no.setStart(CURRENT_N + 1);
        no.setPeriod(0);

        Process[] processes = new Process[] {
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
        query.doTriggeredProcesses(CURRENT_N);

        // Verify that only the triggered process actually took place.
        assertEquals(0, no.getTimesFired());
        assertEquals(1, yes.getTimesFired());
    }

//    private class ProcessManagerExposed extends ProcessManager {
//        public ProcessManagerExposed(ProcessFactory factory, GeneralParameters p) {
//            super(factory, p);
//        }
//
//        @Override
//        public boolean triggered(int n, processes.Process process) {
//            return super.triggered(n, process);
//        }
//    }
}
