package continuum.operations;

import structural.identifiers.Coordinate;
import geometry.Geometry;

public class Diffusion extends ContinuumOperation {

	// Diffusion constant
	private double r;
	
	public Diffusion(Geometry geometry, double r) {
		super(geometry);
		this.r = r;
		buildMatrix();
	}

	@Override
	/**
	 * This is an explicit implementation of the heat equation with
	 * constant coefficient D, with time in units of dt. 
	 * For further explanation, see "Diffusion.pdf" in the Documentation
	 * directory.
	 * 
	 * As a reminder,
	 * 
	 *              n      dt
	 *     a = D * --- * ------
	 *              m    (dx)^2
	 *              
	 * Where "m" is the connectivity of the graph and "n" is the dimensionality 
	 * of the graph.We assume that dt and dx are both equal to unity. (dt is
	 * in fact set to prevent r from exceeding some maximum value.)
	 * 
	 */
	protected void buildMatrix() {
		double n = (double) geometry.getDimensionality();
		double m = (double) geometry.getConnectivity();
	
		// Calculate r. See method javadoc above.
		double a = r * (n / m);
		
		// Calculate matrix diagonal entries.
		double self = 1.0 - 2 * m * a;
				
		for (int i = 0; i < sites.length; i++) {
			Coordinate coord = sites[i];
			
			// Set the diagonal value
			adjust(i, i, self);
			
			// Set each neighbor. For reflecting boundary conditions, one or
			// more neighbors may be the diagonal.
			for (Coordinate neighbor : geometry.getSoluteNeighbors(coord)) {
				// Each of the 2m neighbors get r units of solute.
				int j = coordToIndex.get(neighbor);
				adjust(i, j, r);
			}
		}
	}
	
	private void adjust(int i, int j, double delta) {
		Double current = get(i, j);
	
		set(i, j, current + delta);
	}

}
