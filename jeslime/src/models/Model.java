package models;

import io.serialize.SerializationManager;
import geometries.Geometry;
import structural.GeneralParameters;
import structural.Lattice;
import structural.StateMapViewer;
import structural.halt.*;

public abstract class Model {
	
	protected GeneralParameters p;
	protected Geometry g;
	protected SerializationManager mgr;
	protected Lattice lattice;
	
	public abstract void initialize();
	public abstract HaltCondition go();
	public abstract void postprocess(HaltCondition ex);
	
	public Model(GeneralParameters p, Geometry g, SerializationManager mgr) {
		this.p = p;
		this.g = g;
		this.mgr = mgr;
		
		// Build lattice
		lattice = new Lattice(g);
		
		// Call back to serialization manager
		mgr.nextSimulation(lattice);
	}
	
	protected void checkForFixation() throws FixationEvent {
		StateMapViewer smv = lattice.getStateMapViewer();
		
		for (Integer state : smv.getStates()) {
			if (smv.getCount(state) == g.getSiteCount()) {
				throw new FixationEvent(state, lattice.getGillespie());
			}
		}
		
	}
}
