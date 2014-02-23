package processes.temporal;

import layers.LayerManager;
import processes.Process;
import processes.gillespie.GillespieState;
import geometry.Geometry;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import layers.cell.CellLayer;

/**
 * Convenience superclass for time-specific processes,
 * including some methods for calculating elapsed time.
 * Probably not necessary to have a separate superclass
 * for this.
 * 
 * At the moment, TimeProcess is specific to a particular
 * cell layer.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public abstract class TimeProcess extends Process {
	protected GeneralParameters p;
	
	public TimeProcess(ProcessLoader loader, LayerManager layerManager, int id,
			GeneralParameters p) {
		
		super(loader, layerManager, p, id);
		
		this.p = p;
	}
	
	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}

	public void target(GillespieState gs) {
		// There's only one event that can happen--we update.
		if (gs != null) {
			gs.add(this.getID(), 1, 0.0D);	
		}
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
