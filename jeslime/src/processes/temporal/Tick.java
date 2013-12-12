package processes.temporal;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;

/**
 * Advances the clock by a constant dt.
 * 
 * @author dbborens
 *
 */
public class Tick extends TimeProcess {

	private double dt;
	public Tick(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, layer, id, geom, p);
		
		dt = Double.valueOf(get("dt"));
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		state.advanceClock(dt);
	}

}
