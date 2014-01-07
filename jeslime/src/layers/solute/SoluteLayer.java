package layers.solute;

import continuum.solvers.Solver;
import geometry.Geometry;
import layers.Layer;
import layers.LayerManager;
import no.uib.cipr.matrix.DenseVector;

import java.util.HashMap;

import structural.Flags;
import structural.identifiers.Coordinate;

public abstract class SoluteLayer extends Layer {

	// Current model state
	protected DenseVector state;
	
	// Extrema
	protected Extremum localMin;

	protected Extremum localMax;
	protected Extremum globalMin;
	protected Extremum globalMax;

    protected String id;

	protected HashMap<Coordinate, Integer> coordToIndex;
	
	protected Coordinate dummy = new Coordinate(-1, -1, Flags.UNDEFINED);

    protected LayerManager manager;
    @Override
    public String getId() {
        return id;
    }

    /**
     * Constructor for normal use.
     */
	public SoluteLayer(Geometry geom, LayerManager manager, String id) {
		geometry = geom;
		this.id = id;

        this.manager = manager;

		localMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		globalMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		
		localMax = new Extremum(dummy, -1D, Double.NEGATIVE_INFINITY);
		globalMax = new Extremum(dummy, -1D, Double.NEGATIVE_INFINITY);

	}
	
	public void push(DenseVector state, double t) {
		this.state = state;
		
		updateExtrema(t);
	}
	
	protected void updateExtrema(double t) {
		localMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		globalMin = new Extremum(dummy, -1D, Double.POSITIVE_INFINITY);
		
		Coordinate[] sites = geometry.getCanonicalSites();
		
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
		return geometry;
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

    public abstract Solver getSolver();
}
