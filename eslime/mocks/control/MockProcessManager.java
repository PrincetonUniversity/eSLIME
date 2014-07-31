/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import control.halt.HaltCondition;
import processes.Process;
import processes.StepState;

/**
 * Created by dbborens on 1/13/14.
 */
public class MockProcessManager extends ProcessManager {
    int timesIterated;

    boolean doTriggeredProcessedCalled;
    private Process[] triggeredProcesses;
    private double stepStateDt;

    public MockProcessManager() {
        super();
        timesIterated = 0;
        doTriggeredProcessedCalled = false;
        stepStateDt = 0.0;
    }

    public void setTriggeredProcesses(Process[] triggeredProcesses) {
        this.triggeredProcesses = triggeredProcesses;
    }

    @Override
    protected Process[] getTriggeredProcesses(int n) {
        return triggeredProcesses;
    }

    @Override
    public StepState doTriggeredProcesses(StepState stepState) throws HaltCondition {
        timesIterated++;
        for (Process p : triggeredProcesses) {
            p.fire(stepState);
        }
        stepState.advanceClock(stepStateDt);
        return stepState;
    }

    public void setStepStateDt(double stepStateDt) {
        this.stepStateDt = stepStateDt;
    }

    public int getTimesIterated() {
        return timesIterated;
    }
}
