package control;

import discrete.Process;
import io.project.ProcessLoader;

/**
 * Created by dbborens on 1/6/14.
 */
public class DiscreteProcessManager {
    public DiscreteProcessManager(ProcessLoader processLoader) {

    }

    public Process[] getTriggeredProcesses(int n, double t) {

    }

    protected boolean triggered(int t, Process process) {
        int period = process.getPeriod();
        int start = process.getStart();

        // Case 1: this is a 1-time event, and it is that one time.
        if (period == 0 && start == t) {
            return true;

            // Case 2: this is a 1-time event, and it isn't that time.
        } else if (period == 0 && start != t) {
            return false;

            // Case 3: We haven't reached the start time.
        } else if (t < start) {
            return false;

            // Case 4: We have reached the start time.
        } else if (t >= start) {
            // Adjust phase.
            int tt = t - start;

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
}
