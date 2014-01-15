package control;

import processes.Process;
import processes.StepState;
import structural.halt.HaltCondition;

/**
 * Created by dbborens on 1/13/14.
 */
public class MockProcessManager extends ProcessManager {
    int timesIterated;

    boolean doTriggeredProcessedCalled;

    public MockProcessManager() {
        super();
        timesIterated = 0;
        doTriggeredProcessedCalled = false;
    }

    @Override
    public StepState doTriggeredProcesses(int n) throws HaltCondition {
        timesIterated++;
        return new StepState();
    }


    public int getTimesIterated() {
        return timesIterated;
    }
}
