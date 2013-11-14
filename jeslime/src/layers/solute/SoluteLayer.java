package layers.solute;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;

import java.util.HashMap;

import structural.Flags;
import structural.identifiers.Coordinate;

public class SoluteLayer {

	// Model geometry
	private Geometry geom;
	
	// Current model state
	private DenseVector state;
	
	// Extrema
	private Extremum localMin;

	private Extremum localMax;
	private Extremum globalMin;
	private Extremum globalMax;
	
	private HashMap<Coordinate, Integer> coordToIndex;
	
	private Coordinate dummy = new Coordinate(-1, -1, Flags.UNDEFINED);
	public SoluteLayer(Geometry geom) {
		this.geom = geom;
		
		localMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		globalMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		
		localMax = new Extremum(dummy, -1D, Double.NEGATIVE_INFINITY);
		globalMax = new Extremum(dummy, -1D, Double.NEGATIVE_INFINITY);

	}
	
	public void push(DenseVector state, double t) {
		this.state = state;
		
		updateExtrema(t);
	}
	
	private void updateExtrema(double t) {
		localMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		globalMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		
		Coordinate[] sites = geom.getCanonicalSites();
		
		for (int i = 0; i < sites.length; i++) {
			 double value = state.get(i);
			 Extremum candidate = new Extremum(sites[i], t, value);
			 
			 if (value > localMax.getValue()) {
				 localMax = candidate;
			 }
			 
			 if (value > globalMax.getValue()) {
				 globalMax = candidate;
			 }
			 
			 if (value < localMin.getValue()) {
				 localMin = candidate;
			 }
			 
			 if (value < globalMin.getValue()) {
				 globalMin = candidate;
			 }
		}
	}

	public DenseVector getState() {
		return state;
	}
	
	public Geometry getGeom() {
		return geom;
	}

	public Extremum getLocalMin() {
		return localMin;
	}

	public Extremum getLocalMax() {
		return localMax;
	}

	public Extremum getGlobalMin() {
		return globalMin;
	}

	public Extremum getGlobalMax() {
		return globalMax;
	}

	public HashMap<Coordinate, Integer> getCoordToIndex() {
		return coordToIndex;
	}

}
