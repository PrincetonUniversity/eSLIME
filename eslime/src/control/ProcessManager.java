/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import control.halt.HaltCondition;
import layers.LayerManager;
import processes.EcoProcess;
import processes.StepState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbborens on 1/6/14.
 */
public class ProcessManager {

    private List<EcoProcess> processes;
    private LayerManager layerManager;

    public ProcessManager(List<EcoProcess> processes, LayerManager layerManager) {
        this.processes = processes;
        this.layerManager = layerManager;
    }

    protected List<EcoProcess> getTriggeredProcesses(int n) throws HaltCondition {

        ArrayList<EcoProcess> triggeredProcesses = new ArrayList<>(processes.size());

        for (EcoProcess process : processes) {
            if (triggered(n, process)) {
                triggeredProcesses.add(process);
            }
        }

        return triggeredProcesses;
    }

    protected boolean triggered(int n, EcoProcess process) throws HaltCondition {
        int period = process.getPeriod().next();
        int start = process.getStart().next();

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
        List<EcoProcess> triggeredProcesses = getTriggeredProcesses(stepState.getFrame());

        // Fire each triggered cell event.
        for (EcoProcess process : triggeredProcesses) {
            process.iterate();
        }

        // There's no reason for the layer manager to touch the StepState
        // object until the next cycle. If it does, the program should blow up,
        // so we have it throw a null pointer exception.
        layerManager.setStepState(null);

        return stepState;
    }

    /**
     * Resets all layers and processes to their original
     * configurations.
     */
    public void init() {
        layerManager.reset();
        for (EcoProcess process : processes) {
            process.init();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ProcessManager)) {
            return false;
        }

        ProcessManager other = (ProcessManager) obj;

        if (other.processes.size() != this.processes.size()) {
            return false;
        }

        for (int i = 0; i < processes.size(); i++) {
            EcoProcess mine = processes.get(i);
            EcoProcess theirs = other.processes.get(i);

            if (!mine.equals(theirs)) {
                return false;
            }
        }

        return true;
    }
}
