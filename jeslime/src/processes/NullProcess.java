package processes;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

/**
 * Dummy process that does nothing except report that it
 * was invoked. Used for testing.
 *
 * @untested
 * @author David Bruce Borenstein
 *
 */
public class NullProcess extends Process {

    private final Integer count;
    private final Double weight;

    // Some identifier to let the user distinguish between null processes
	private final String identifier;

    // Tells the user how many times the process was invoked. Useful for
    // testing.
    private int timesFired = 0;

	public NullProcess(ProcessLoader loader, int id, Geometry geom) {
		super(loader, id, geom);

        identifier = get("identifier");
		weight = Double.valueOf(get("weight", "1.0"));
		count = Integer.valueOf(get("count", "1"));
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
        timesFired++;
		System.out.println("   Fired null event " + getID() + ".");
	}

    public int getTimesFired() {
        return timesFired;
    }

    public String getIdentifier() {
        return identifier;
    }
}
