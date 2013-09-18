package io.serialize;

import geometries.Geometry;
import structural.Lattice;
import structural.GeneralParameters;
import structural.identifiers.Coordinate;

public abstract class TemporalMetricWriter extends Writer {

	protected Lattice lattice;
	protected Geometry geometry;
	protected GeneralParameters p;
	
	public TemporalMetricWriter(GeneralParameters p, Lattice lattice, Geometry geometry) {
		this.lattice = lattice;
		this.geometry = geometry;
		this.p = p;
	}
	
	/**
	 * Initialize data structures.
	 */
	public abstract void init();
	
	/**
	 * Trigger an update event.
	 */
	public abstract void push(Coordinate[] highlights, double gillespie, int frame);
	
	/**
	 * Conclude analysis, finish writing to files, and close.
	 */
	public abstract void close();
}
