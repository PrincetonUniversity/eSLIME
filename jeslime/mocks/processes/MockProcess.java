package processes;

import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

/**
 * Dummy process that does nothing except report that it
 * was invoked. Used for testing.
 *
 * @author David Bruce Borenstein
 *
 */
public class MockProcess extends Process {

    private Integer count;
    private Double weight;
    private int start;
    private int period;

    // Some identifier to let the user distinguish between null processes
	private String identifier;

    // Tells the user how many times the process was invoked. Useful for
    // testing.
    private int timesFired = 0;

	public MockProcess(ProcessLoader loader, LayerManager layerManager, int id) {
		super(loader, layerManager, id);

        identifier = get("identifier");
		weight = Double.valueOf(get("weight", "1.0"));
		count = Integer.valueOf(get("count", "1"));
	}

    public MockProcess() {
        super(null, null, 0);
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public void setTimesFired(int timesFired) {
        this.timesFired = timesFired;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}