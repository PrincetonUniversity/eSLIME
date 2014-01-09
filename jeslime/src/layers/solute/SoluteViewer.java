package layers.solute;

import geometry.Geometry;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;

public class SoluteViewer {

	private SoluteLayer layer;
	private Geometry geom;
	
	public SoluteViewer(SoluteLayer layer, Geometry geom) {
		this.layer = layer;
		this.geom = geom;
	}
	
	public double get(Coordinate coord, boolean relative) {
	    if (relative) {
            return layer.getState().getRelative(coord);
        } else {
		    return layer.getState().getAbsolute(coord);
        }
	}

	public void push(SolutionViewer state) {
		layer.push(state);
	}
}
