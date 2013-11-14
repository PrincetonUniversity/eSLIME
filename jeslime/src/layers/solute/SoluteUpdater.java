package layers.solute;

import no.uib.cipr.matrix.DenseVector;

public class SoluteUpdater {

	private SoluteLayer layer;
	
	public SoluteUpdater(SoluteLayer layer) {
		this.layer = layer;
	}
	
	public void push(DenseVector state, double t) {
		layer.push(state, t);
	}
}
