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
    public StepState doTriggeredProcesses(int n, StepState stepState) throws HaltCondition {
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
