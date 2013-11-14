package processes.gillespie;

import java.util.HashMap;
import java.util.Random;

import org.dom4j.Element;

import geometry.Geometry;
import io.project.ProcessFactory;
import io.project.ProcessLoader;
import processes.StepState;
import processes.cellular.CellProcess;
import processes.Process;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;

/**
 * Sets up and executes a Gillespie process. This process takes a list of
 * child processes. It calls target() on all of them, 
 * 
 * Note that a Gillespie process
 * can be a child of a Gillespie process. In which case, it passes its own
 * total weight and event count up to the GillespieState of the parent
 * process.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public class GillespieProcess extends CellProcess {

	private Process[] children;
	private HashMap<Integer, Process> childrenById;
	private GillespieState substate;
	
	public GillespieProcess(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, layer, id, geom, p);
		
		loadChildren(loader);
	}

	private void loadChildren(ProcessLoader loader) {
		Element root = e.element("children");
		
		ProcessLoader subloader = new ProcessLoader(root);

		ProcessFactory factory = new ProcessFactory(subloader, layer, p, geom);
		
		Integer[] ids = subloader.getProcesses();
		
		childrenById = new HashMap<Integer, Process>(ids.length);

		for (Integer id : ids) {
			Process child = factory.instantiate(id);
			childrenById.put(id, child);
		}
		
		children = childrenById.values().toArray(new Process[0]);
	}
	
	public void target(GillespieState superstate) throws HaltCondition {
		Integer[] ids = childrenById.keySet().toArray(new Integer[0]);
		substate = new GillespieState(ids);
		for (Process child : children) {
			child.target(substate);
		}
		
		substate.close();
		
		double weight = substate.getTotalWeight();
		int count = substate.getTotalCount();
		
		// Superstate represents the Gillespie state of the calling Gillespie
		// process, if there is one. In that case, this entire Gillespie process
		// is treated as one possible "event" in the parent process, and the total
		// weight and count of all possible events in this process are reported as
		// the event weight and count to the parent process.
		if (superstate != null) {
			superstate.add(getID(), count, weight);
		}
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		if (substate == null) {
			throw new IllegalStateException("Called fire() before calling target().");
		}
		
		// If no events, don't do anything.
		if (substate.getTotalCount() == 0) {
			return;
		}
		
		// Choose an event
		GillespieChooser chooser = new GillespieChooser(substate);
		double x = p.getRandom().nextDouble() * substate.getTotalWeight();
		Integer id = chooser.selectTarget(x);
		Process chosen = childrenById.get(id);
		
		// Fire the event
		chosen.fire(state);
		
		// Calculate lambda for exponential waiting time. All events have
		// non-zero weight, so there should be no divide by zero.
		double lambda = 1.0D / substate.getTotalWeight();
		
		// Calculate waiting time
		double dt = expRandom(lambda);
		
		// Apply it to the state object
		state.advanceClock(dt);
	}
	
	/**
	 * Returns an exponentially distributed random number.
	 */
	protected double expRandom(double lambda) {
		// Get a random number between 0 (inc) and 1 (exc)
		double u = p.getRandom().nextDouble();
		
		// Inverse of exponential CDF
		return Math.log(1 - u) / (-1 * lambda);
	}
}
