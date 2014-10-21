/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control;

import control.halt.HaltCondition;
import control.halt.StepMaxReachedEvent;
import io.serialize.SerializationManager;
import processes.StepState;

public class Integrator {

    private final ProcessManager processManager;
    private GeneralParameters p;
    private SerializationManager serializationManager;

    protected double time = 0.0D;

    public Integrator(GeneralParameters p, ProcessManager processManager,
                      SerializationManager serializationManager) {

        // Assign member variables.
        this.p = p;
        this.serializationManager = serializationManager;
        this.processManager = processManager;
    }

    /**
     * Run all iterations, including the initial condition (t=0),
     * updating any solutes and cells, as well as advancing the clock,
     * according to the processes specified in the project file.
     *
     * @return
     */
    public HaltCondition go() {
        for (int n = 0; n < p.T(); n++) {
            StepState state = new StepState(time, n);
            try {
                state = processManager.doTriggeredProcesses(state);
            } catch (HaltCondition haltCondition) {
                haltCondition.setGillespie(state.getTime());
                return haltCondition;
            }

            // Send the results to the serialization manager.
            serializationManager.flush(state);
            time = state.getTime();
        }

        // If we got here, it's because we got through the outermost
        // loop, which proceeds for a specified number of iterations
        // before terminating. (This prevents infinite loops.)
        StepMaxReachedEvent ret = new StepMaxReachedEvent();
        ret.setGillespie(time);
        return ret;
    }
}
