package processes;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

/**
 * Dummy process that does nothing except report that it
 * was invoked.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public class NullProcess extends Process {

	private double weight;
	private int count;
	
	public NullProcess(ProcessLoader loader, int id, Geometry geom) {
		super(loader, id, geom);
		
		weight = Double.valueOf(get("weight"));
		count = Integer.valueOf(get("count"));
	}

	@Override
	protected String getProcessClass() {
		return getClass().getSimpleName(); 
	}

	@Override
	public void target(GillespieState gs) throws HaltCondition {
		if (gs != null) {
			gs.add(getID(), count, weight);
		}
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		System.out.println("   Fired null event " + getID() + ".");
	}

}
