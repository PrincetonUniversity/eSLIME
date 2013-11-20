package continuum.operations;

import org.dom4j.Element;

import structural.identifiers.Coordinate;
import geometry.Geometry;

public class Diffusion extends Operator {

	// Diffusion constant
	private double r;
	
	public Diffusion(Geometry geometry, boolean useBoundaries, double r) {
		super(geometry, useBoundaries);
		this.r = r;
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
	public void init() {
		double n = (double) dimension();
		double m = (double) connectivity();
	
		// Calculate r. See method javadoc above.
		double a = r * (n / m);
		
		// Calculate matrix diagonal entries.
		double self = 1.0 - 2 * m * a;
				
		for (int i = 0; i < sites.length; i++) {
			Coordinate coord = sites[i];
			
			// Set the diagonal value
			augment(i, i, self);
			
			// Set each neighbor. For reflecting boundary conditions, one or
			// more neighbors may be the diagonal.
			for (int j : neighbors(coord)) {
				// Each of the 2m neighbors get r units of solute.
				augment(i, j, r);
			}
		}
	}
	
	public double getR() {
		return r;
	}

}
