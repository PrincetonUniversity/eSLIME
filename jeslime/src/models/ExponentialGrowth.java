package models;

import io.BufferedStateWriter;
import control.Parameters;
import processes.DivideAnywhere;
import processes.Scatter;
import structural.HaltException;
import structural.Lattice;
import structural.identifiers.Coordinate;
import geometries.*;

public class ExponentialGrowth extends Model {

	private Parameters p;
	private BufferedStateWriter bsw;
	private Geometry geom;
	private Lattice lattice;
	private Scatter scatter;
	private DivideAnywhere division;
	
	public ExponentialGrowth(Parameters p) {
		this.p = p;
		
		// Build geometry
		geom = new HexRing(p.W(), p.W());
		
		// Build lattice
		lattice = new Lattice(geom);
		
		// Build state writer
		bsw = new BufferedStateWriter(p, geom);
		
		// Build scatter transform
		scatter = new Scatter(lattice, 5, 10);
		
		// Build growth transform
		division = new DivideAnywhere(lattice, geom);
	}
	
	public void initialize() {
		// Scatter some cells
		System.out.print("Initializing...");
		
		Coordinate[] highlight;
		try {
			highlight = scatter.iterate();
		} catch (HaltException ex) {
			ex.printStackTrace();
			System.err.println("Halting simulation.");
		}
		System.out.println("done.");
		
		System.out.print("Recording...");
		bsw.push(lattice.getStateVector(), lattice.getFitnessVector(), lattice.getGillespieTime());
		System.out.println("done.");
	}
	
	public void go() {
		for (int i = 0; i < MAX_STEP; i++) {
			System.out.println("Step " + i );
			
			System.out.print("   Iterating...");
			
			Coordinate[] highlight;
			
			try {
				highlight = division.iterate();
			} catch (HaltException ex) {
				ex.printStackTrace();
				System.err.println("Halting simulation.");
				break;
			}
			
			System.out.println("done.");
			
			System.out.println("   Recording...");
			bsw.push(lattice.getStateVector(), lattice.getFitnessVector(), lattice.getGillespieTime());
		}
		
		conclude();
	}
	
	private void conclude() {
		bsw.close();
		System.out.println("Simulation ended.");
	}
}
