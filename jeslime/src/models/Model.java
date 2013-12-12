package models;

import layers.cell.CellLayer;
import layers.cell.StateMapViewer;
import processes.Process;
import processes.StepState;
import io.project.ProcessFactory;
import io.project.ProcessLoader;
import io.serialize.SerializationManager;
import geometry.Geometry;
import structural.GeneralParameters;
import structural.halt.*;

public class Model {
	
	private GeneralParameters p;
	private Geometry g;
	private SerializationManager mgr;
	private CellLayer layer;
	
	private Process[] processes;
	
	private double time = 0.0D;
	
	public Model(GeneralParameters p, ProcessLoader loader, Geometry g,
			SerializationManager mgr) {
		
		// Assign member variables.
		this.p = p;
		this.g = g;
		this.mgr = mgr;
		
		// Build lattice.
		layer = new CellLayer(g, 0);
		
		// Build process factory.
		ProcessFactory factory = new ProcessFactory(loader, layer, p, g);
		
		Integer[] ids = loader.getProcesses();
		processes = new Process[ids.length];
		
		// Build processes.
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			processes[i] = factory.instantiate(id);
		}
			
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

		for (int t = 0; t < p.T(); t++) {
			StepState state = new StepState();

			for (Process process : processes) {
				try {
					
	
					// A period of 0 means that the process should occur
					// at initialization and no other time. Only processes
					// with a period of 0 are called at initialize.
					if (triggered(t, process)) {
						process.iterate(state);
					}
				} catch (HaltCondition hc) {
					return hc;
				}

			}
			
			// Check to see if the system has entered a fixation state.
			try {
				checkForFixation();
			} catch (FixationEvent ex) {
				return ex;
			}
			
			// Signal to state object that we are done modifying it.
			state.close();

			// Send the results to the serialization manager.
			mgr.step(state.getHighlights(), state.getDt(), t);
			time += state.getDt();
		}
		
		// If we got here, it's because we got through the outermost
		// loop, which proceeds for a specified number of iterations
		// before terminating. (This prevents infinite loops.)
		return new StepMaxReachedEvent(time);
		
	}
	private boolean triggered(int t, Process process) {
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

	protected void checkForFixation() throws FixationEvent {
		StateMapViewer smv = layer.getViewer().getStateMapViewer();
		
		for (Integer state : smv.getStates()) {
			if (smv.getCount(state) == g.getCanonicalSites().length) {
				throw new FixationEvent(state, time);
			}
		}
	}
	
	private void conclude(HaltCondition ex) {
		//System.out.println("Simulation ended.");
	}
	
	public void postprocess(HaltCondition ex) {
		conclude(ex);
	}
}
