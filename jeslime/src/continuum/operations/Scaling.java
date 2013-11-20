package continuum.operations;

import geometry.Geometry;

/**
 * Returns an identity matrix scaled by lambda.
 * 
 * @author David Bruce Borenstein
 * @test ScalingTest
 */
public class Scaling extends Operator {

	private double lambda;
	
	public Scaling(Geometry geometry, boolean useBoundaries, double lambda) {
		super(geometry, useBoundaries);
		this.lambda = lambda;
	}

	@Override
	public void init() {
		for (int i = 0; i < sites.length; i++) {
			set(i, i, lambda);
		}
	}
	
	public double getLambda() {
		return lambda;
	}

}
