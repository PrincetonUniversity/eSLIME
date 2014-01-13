package control;

import io.project.GeometryManager;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.StateMapViewer;
import io.project.ProcessLoader;
import io.serialize.SerializationManager;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.*;
import processes.Process;

public class Integrator {

    private final ProcessManager processManager;
    private GeneralParameters p;
	private GeometryManager gm;
	private SerializationManager mgr;
	private CellLayer layer;

	private double time = 0.0D;
	
	public Integrator(GeneralParameters p, ProcessLoader loader,
                      GeometryManager gm, SerializationManager mgr,
                      LayerManager lm) {
		
		// Assign member variables.
		this.p = p;
		this.gm = gm;
		this.mgr = mgr;

        this.processManager = new ProcessManager(loader, lm, p);
		// Build lattice.
		layer = lm.getCellLayer();


		// Call back to serialization manager.
		mgr.nextSimulation(layer);
	}
	
	/**
	 * Run all iterations, including the initial condition (t=0), 
	 * updating any solutes and cells, as well as advancing the clock,
	 * according to the processes specified in the project file.
	 * @return
	 */
	public HaltCondition go() {

		for (int n = 0; n < p.T(); n++) {
            StepState state = new StepState();

            // Get triggered events.
            Process[] triggeredProcesses = processManager.getTriggeredProcesses(n, time);

            // Fire each triggered cell event.
            try {
                for (Process process : triggeredProcesses) {
                    process.iterate(state);
                }

                checkForFixation();
            } catch (HaltCondition hc) {
                return hc;
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


	protected void checkForFixation() throws FixationEvent {
        throw new IllegalStateException("Move checkForFixation to its own process");
		/*StateMapViewer smv = layer.getViewer().getStateMapViewer();
		
		for (Integer state : smv.getStates()) {
			if (smv.getCount(state) == layer.getGeometry().getCanonicalSites().length) {
				throw new FixationEvent(state, time);
			}
		}*/
	}
	
	private void conclude(HaltCondition ex) {
		//System.out.println("Simulation ended.");
	}
	
	public void postprocess(HaltCondition ex) {
		conclude(ex);
	}
}
