package models;

import io.serialize.BufferedStateWriter;
import io.serialize.SerializationManager;
import processes.DivideAnywhere;
import processes.Scatter;
import structural.Lattice;
import structural.GeneralParameters;
import structural.halt.*;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;
import geometries.*;

public class ExponentialGrowth extends Model {

	private Scatter scatter;
	private DivideAnywhere division;
	
	public ExponentialGrowth(GeneralParameters p, Geometry geom, SerializationManager mgr) {
		super(p, geom, mgr);

		// Build scatter transform
		scatter = new Scatter(lattice, 5, 10);
		
		// Build growth transform
		division = new DivideAnywhere(lattice, geom);
	}
	
	public void initialize() {

		System.out.println("done.");
		
		// Scatter some cells
		System.out.print("Initializing simulation...");
		
		Coordinate[] highlight = new Coordinate[0];
		try {
			highlight = scatter.iterate();
		} catch (HaltCondition ex) {
			ex.printStackTrace();
			System.err.println("Halting simulation.");
		}
		System.out.println("done.");
		
		System.out.print("Recording...");
		mgr.step(highlight, 0D, 0);
		System.out.println("done.");
	}
	
	public HaltCondition go() {
		for (int i = 0; i < p.T(); i++) {
			Coordinate[] highlight;
			
			try {
				highlight = division.iterate();
			} catch (HaltCondition ex) {
				//ex.printStackTrace();
				//System.err.println("Halting simulation.");
				return ex;
			}
			lattice.advanceClock(1.0);
			mgr.step(highlight, lattice.getGillespie(), i+1);
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
