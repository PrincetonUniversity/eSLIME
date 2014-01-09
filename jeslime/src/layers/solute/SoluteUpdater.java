package layers.solute;

import no.uib.cipr.matrix.DenseVector;
import structural.postprocess.SolutionViewer;

public class SoluteUpdater {

	private SoluteLayer layer;
	
	public SoluteUpdater(SoluteLayer layer) {
		this.layer = layer;
	}
	
	public void push(SolutionViewer state) {
		layer.push(state);
	}
}
