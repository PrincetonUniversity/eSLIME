package models;

import io.serialize.SerializationManager;
import processes.*;
import structural.GeneralParameters;
import structural.halt.*;
import structural.identifiers.Coordinate;
import geometries.*;

public class ExponentialGrowth extends Model {

	private Scatter scatter;
	private DivideAnywhere divide;
	private NeighborSwap swap;
	
	private final int SWAP_PERIOD = 10;
	public ExponentialGrowth(GeneralParameters p, Geometry geom, SerializationManager mgr) {
		super(p, geom, mgr);

		// Build scatter process
		scatter = new Scatter(lattice, 2, 1);
		
		// Build growth process
		divide = new DivideAnywhere(lattice, geom);
		
		// Build swap process
		swap = new NeighborSwap(lattice, geom, p);
	}
	
	public void initialize() {

		// Scatter some cells
		
		Coordinate[] highlight = new Coordinate[0];
		try {
			highlight = scatter.iterate();
			
		} catch (HaltCondition ex) {
			ex.printStackTrace();
			System.err.println("Halting simulation.");
		}
		mgr.step(highlight, 0D, 0);
	}
	
	public HaltCondition go() {
		for (int i = 0; i < p.T(); i++) {
			Coordinate[] highlight;
			
			try {
				highlight = divide.iterate();
				
				if (i % SWAP_PERIOD == 0) {
					swap.iterate();
				}
			} catch (HaltCondition ex) {
				//ex.printStackTrace();
				//System.err.println("Halting simulation.");
				return ex;
			}
			lattice.advanceClock(1.0);
			mgr.step(highlight, lattice.getGillespie(), i+1);
	
			try {
				checkForFixation();
			} catch (FixationEvent ex) {
				return ex;
			}
		}
		
		return new StepMaxReachedEvent(lattice.getGillespie());
	}




	private void conclude(HaltCondition ex) {
		//System.out.println("Simulation ended.");
	}
	
	public void postprocess(HaltCondition ex) {
		conclude(ex);
	}
}
