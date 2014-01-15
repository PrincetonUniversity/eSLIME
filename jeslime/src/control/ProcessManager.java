package control;

import io.project.ProcessFactory;
import processes.Process;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

import java.util.ArrayList;

/**
 * Created by dbborens on 1/6/14.
 */
public class ProcessManager {

    private ProcessFactory factory;
    private GeneralParameters p;

    private Process[] processes;

    /**
     * Default constructor included for testing
     */
    public ProcessManager() {}

    public ProcessManager(ProcessFactory factory, GeneralParameters p) {

        this.p = p;

        processes = factory.getProcesses();
    }

    //protected Process[] getTriggeredProcesses(int n, double t) {
    protected Process[] getTriggeredProcesses(int n) {
        // Currently, no handling for time-triggered processes

        ArrayList<Process> triggeredProcesses = new ArrayList<>(processes.length);

        for (Process process: processes) {
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

    public StepState doTriggeredProcesses(int n) throws HaltCondition {
        StepState state = new StepState();

        // Get triggered events.
        Process[] triggeredProcesses = getTriggeredProcesses(n);

        // Fire each triggered cell event.
        for (Process process : triggeredProcesses) {
            process.iterate(state);
        }

        return state;
    }
}
