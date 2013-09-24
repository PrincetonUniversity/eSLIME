package io.serialize;

import java.util.HashSet;
import java.util.Set;

import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;
import geometries.Geometry;

public class SerializationManager {

	private Set<Writer> writers;
	private GeneralParameters p;
	private Geometry g;
	
	public SerializationManager(GeneralParameters p, Geometry g) {
		this.p = p;
		this.g = g;
		
		writers = new HashSet<Writer>();
		
		if (p.isWriteState()) {
			BufferedStateWriter bsw = new BufferedStateWriter(p, g);
			writers.add(bsw);
		}
		
		if (p.isStateHisto()) {
			StateHistogram sh = new StateHistogram(p, g);
			writers.add(sh);
		}
		
		if (p.isWriteFixTime()) {
			FixationTimeWriter ftw = new FixationTimeWriter(p, g);
			writers.add(ftw);
		}
	}
	
	/**
	 * Initialize all writers.
	 */
	public void init(Lattice l) {
		for (Writer tw : writers) {
			tw.init(l);
		}
	}
	
	/**
	 * Advance current instance by one time step.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		for (Writer tw : writers) {
			tw.step(highlights, gillespie, frame);
		}	
	}
	
	/**
	 * Opens handles / initializes data structures for a new instance.
	 * Blows up if these were left open from the previous instance.
	 */
	public void nextSimulation(Lattice l) {
		for (Writer tw : writers) {
			tw.init(l);
		}	
	}
	
	/**
	 * Conclude the entire simulation project.
	 */
	public void close() {
		for (Writer tw : writers) {
			tw.close();
		}	
	}

	public void dispatchHalt(HaltCondition ex) {
		System.out.println("Simulation ended. Cause: " + ex.getClass().getSimpleName());

		for (Writer tw : writers) {
			tw.dispatchHalt(ex);
		}
		
		if (p.isLineageMap()) {
			ImageSequence imgSequence = new ImageSequence(p.getInstancePath(), g, p);
			imgSequence.generate();
		}		
	}
}
