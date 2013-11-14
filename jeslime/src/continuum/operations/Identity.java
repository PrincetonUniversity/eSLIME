package continuum.operations;

import geometry.Geometry;

public class Identity extends ContinuumOperation {

	private double lambda;
	
	public Identity(Geometry geometry, double lambda) {
		super(geometry);
		this.lambda = lambda;
		buildMatrix();
	}

	@Override
	protected void buildMatrix() {
		for (int i = 0; i < sites.length; i++) {
			set(i, i, lambda);
		}
	}

}
