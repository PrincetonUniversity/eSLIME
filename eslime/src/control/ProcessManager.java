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
import factory.processes.ProcessFactory;
import layers.LayerManager;
import processes.Process;
import processes.StepState;

import java.util.ArrayList;

/**
 * Created by dbborens on 1/6/14.
 */
public class ProcessManager {

    private Process[] processes;
    private LayerManager layerManager;

    /**
     * Default constructor included for testing
     */
    public ProcessManager() {
    }

    public ProcessManager(ProcessFactory factory, LayerManager layerManager) {
        processes = factory.getProcesses();
        this.layerManager = layerManager;
    }

    //protected Process[] getTriggeredProcesses(int n, double t) {
    protected Process[] getTriggeredProcesses(int n) {
        // Currently, no handling for time-triggered processes

        ArrayList<Process> triggeredProcesses = new ArrayList<>(processes.length);

        for (Process process : processes) {
            if (triggered(n, process)) {
                triggeredProcesses.add(process);
            }
        }

        return triggeredProcesses.toArray(new Process[0]);
    }

    protected boolean triggered(int n, Process process) {
        int period = process.getPeriod();
        int start = process.getStart();

        // Case 1: this is a 1-time event, and it is that one time.
        if (period == 0 && start == n) {
            return true;

            // Case 2: this is a 1-time event, and it isn't that time.
        } else if (period == 0 && start != n) {
            return false;

            // Case 3: We haven't reached the start time.
        } else if (n < start) {
            return false;

            // Case 4: We have reached the start time.
        } else if (n >= start) {
            // Adjust phase.
            int tt = n - start;

            // 4a: Phase-adjusted time fits period.
            if (tt % period == 0) {
                return true;

                // 4b: It doesn't.
            } else {
                return false;
            }

        } else {
            throw new IllegalStateException("Unconsidered trigger state reached.");
        }
    }

    public StepState doTriggeredProcesses(StepState stepState) throws HaltCondition {

        // Pass the step state object to the layer manager. This way, both actions
        // and processes can access it.
        layerManager.setStepState(stepState);

        // Get triggered events.
        Process[] triggeredProcesses = getTriggeredProcesses(stepState.getFrame());

        // Fire each triggered cell event.
        for (Process process : triggeredProcesses) {
            process.iterate();
        }

        // There's no reason for the layer manager to touch the StepState
        // object until the next cycle. If it does, the program should blow up,
        // so we have it throw a null pointer exception.
        layerManager.setStepState(null);

        return stepState;
    }
}
