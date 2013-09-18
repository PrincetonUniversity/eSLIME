package models;

import io.serialize.BufferedStateWriter;
import io.serialize.SerializationManager;
import processes.DivideAnywhere;
import processes.Scatter;
import structural.Lattice;
import structural.GeneralParameters;
import structural.exceptions.HaltException;
import structural.identifiers.Coordinate;
import geometries.*;

public class ExponentialGrowth extends Model {

	private GeneralParameters p;
	private SerializationManager mgr;
	private Geometry geom;
	private Lattice lattice;
	private Scatter scatter;
	private DivideAnywhere division;
	
	public ExponentialGrowth(GeneralParameters p) {
		this.p = p;
		
		// Build geometry
		//geom = new HexRing(p.W(), p.W());
		geom = new HexArena(p.W(), p.W());

		// Build lattice
		lattice = new Lattice(geom);
		
		// Build serialization manager (output)
		mgr = new SerializationManager(p, lattice, geom);
		
		// Build scatter transform
		scatter = new Scatter(lattice, 5, 10);
		
		// Build growth transform
		division = new DivideAnywhere(lattice, geom);
	}
	
	public Geometry getGeometry() {
		return geom;
	}
	
	public void initialize() {
		System.out.print("Initializing serialization manager...");
		// Initialize output manager
		mgr.init();
		System.out.println("done.");
		
		// Scatter some cells
		System.out.print("Initializing simulation...");
		
		Coordinate[] highlight = new Coordinate[0];
		try {
			highlight = scatter.iterate();
		} catch (HaltException ex) {
			ex.printStackTrace();
			System.err.println("Halting simulation.");
		}
		System.out.println("done.");
		
		System.out.print("Recording...");
		mgr.step(highlight, 0D, 0);
		System.out.println("done.");
	}
	
	public void go() {
		for (int i = 0; i < p.T(); i++) {
			Coordinate[] highlight;
			
			try {
				highlight = division.iterate();
			} catch (HaltException ex) {
				ex.printStackTrace();
				System.err.println("Halting simulation.");
				break;
			}
			mgr.step(highlight, (i + 1.0D) * 1.0D, i+1);

		}
		
		conclude();
	}
	
	private void conclude() {
		mgr.close();
		System.out.println("Simulation ended.");
	}
}
