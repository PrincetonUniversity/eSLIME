/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import control.halt.HaltCondition;
import processes.StepState;

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
        stepStateDt = 0.0;
    }

    @Override
    public StepState doTriggeredProcesses(StepState stepState) throws HaltCondition {
        timesIterated++;
        stepState.advanceClock(stepStateDt);
        return stepState;
    }

    private double stepStateDt;

    public void setStepStateDt(double stepStateDt) {
        this.stepStateDt = stepStateDt;
    }

    public int getTimesIterated() {
        return timesIterated;
    }
}
