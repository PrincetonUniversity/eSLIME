package io.serialize;

import java.util.HashSet;
import java.util.Set;

import structural.GeneralParameters;
import structural.Lattice;
import structural.identifiers.Coordinate;
import geometries.Geometry;

public class SerializationManager {

	private Set<TemporalMetricWriter> instanceWriters;
	private Set<AggregateMetricWriter> aggregateWriters;
	
	public SerializationManager(GeneralParameters p, Lattice l, Geometry g) {
		
		instanceWriters = new HashSet<TemporalMetricWriter>();
		aggregateWriters = new HashSet<AggregateMetricWriter>();
		
		if (p.isWriteState()) {
			BufferedStateWriter bsw = new BufferedStateWriter(p, l, g);
			instanceWriters.add(bsw);
		}
		
		if (p.isStateHisto()) {
			StateHistogram sh = new StateHistogram(p, l, g);
			instanceWriters.add(sh);
		}
	}
	
	/**
	 * Initialize all writers.
	 */
	public void init() {
		for (TemporalMetricWriter tw : instanceWriters) {
			tw.init();
		}
		
		for (AggregateMetricWriter aw : aggregateWriters) {
			aw.init();
		}
	}
	
	/**
	 * Advance current instance by one time step.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		for (TemporalMetricWriter tw : instanceWriters) {
			tw.push(highlights, gillespie, frame);
		}	
	}
	
	/**
	 * Advance current simulation project by one instance,
	 * first closing the current instance.
	 */
	public void advance() {
		for (TemporalMetricWriter tw : instanceWriters) {
			tw.close();
		}	
	}
	
	/**
	 * Conclude the entire simulation project.
	 */
	public void close() {
		for (TemporalMetricWriter tw : instanceWriters) {
			tw.close();
		}	
	}
}
