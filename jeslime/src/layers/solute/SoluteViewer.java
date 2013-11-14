package layers.solute;

import no.uib.cipr.matrix.DenseVector;
import geometry.Geometry;
import structural.identifiers.Coordinate;

public class SoluteViewer {

	private SoluteLayer layer;
	private Geometry geom;
	
	public SoluteViewer(SoluteLayer layer, Geometry geom) {
		this.layer = layer;
		this.geom = geom;
	}
	
	public double get(Coordinate coord) {
		Integer index = geom.coordToIndex(coord);
		
		return layer.getState().get(index);
	}
	
	public Extremum getLocalMinimum() {
		return layer.getLocalMin();
	}
	
	public Extremum getLocalMaximum() {
		return layer.getLocalMax();
	}
	
	public Extremum getGlobalMinimum() {
		return layer.getGlobalMin();
	}
	
	public Extremum getGlobalMaximum() {
		return layer.getGlobalMax();
	}
	
	public void push(DenseVector state, double t) {
		layer.push(state, t);
	}
}
