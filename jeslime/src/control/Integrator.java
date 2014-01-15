package control;

import io.serialize.SerializationManager;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.*;

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

		// Call back to serialization manager.
		mgr.nextSimulation();
	}
	
	/**
	 * Run all iterations, including the initial condition (t=0), 
	 * updating any solutes and cells, as well as advancing the clock,
	 * according to the processes specified in the project file.
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
