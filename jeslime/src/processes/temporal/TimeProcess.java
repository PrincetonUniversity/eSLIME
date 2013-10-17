package processes.temporal;

import processes.Process;
import processes.gillespie.GillespieState;
import geometries.Geometry;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import structural.Lattice;

public abstract class TimeProcess extends Process {
	protected Lattice lattice;
	protected GeneralParameters p;
	
	public TimeProcess(ProcessLoader loader, Lattice lattice, int id, 
			Geometry geom, GeneralParameters p) {
		
		super(loader, id, geom);
		
		this.p = p;
		this.lattice = lattice;
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
