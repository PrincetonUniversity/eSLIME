package continuum.operations;

import structural.identifiers.Coordinate;
import geometry.Geometry;

public class Advection extends ContinuumOperation {

	private Coordinate displacement;
	private double r;
	
	public Advection(Geometry geometry, boolean useBoundaries, Coordinate displacement, double r) {
		super(geometry, useBoundaries);
		this.displacement = displacement;
		this.r = r;
	}

	@Override
	public void init() {
		for (Coordinate origin : sites) {
			// Set origin to whatever isn't removed.
			int j = geometry.coordToIndex(origin);
			set(j, j, 1.0 - r);
			
			// Now weight the target by r.
			Coordinate target = geometry.rel2abs(origin, displacement, mode());
			
			// Handle absorbing boundaries.
			if (target != null) {
				//System.out.println("   Trying to look up " + target);
				int i = geometry.coordToIndex(target);
				set(i, j, r);
			}
		}
	}
}
