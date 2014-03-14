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

import io.serialize.SerializationManager;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.halt.StepMaxReachedEvent;

public class Integrator {

    private final ProcessManager processManager;
    private GeneralParameters p;
    private SerializationManager mgr;

    private double time = 0.0D;

    public Integrator(GeneralParameters p, ProcessManager processManager,
                      SerializationManager mgr) {

        // Assign member variables.
        this.p = p;
        this.mgr = mgr;

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
            StepState state;
            try {
                state = processManager.doTriggeredProcesses(n);
            } catch (HaltCondition haltCondition) {
                return haltCondition;
            }

            // Signal to state object that we are done modifying it.
            state.close();

            // Send the results to the serialization manager.
            mgr.step(state.getHighlights(), state.getDt(), n);
            time += state.getDt();
        }

        // If we got here, it's because we got through the outermost
        // loop, which proceeds for a specified number of iterations
        // before terminating. (This prevents infinite loops.)
        return new StepMaxReachedEvent(time);

    }
}
