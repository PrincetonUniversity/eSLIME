package continuum.operations;

import structural.identifiers.Coordinate;
import geometry.Geometry;

public class Diffusion extends Operator {

	// Diffusion constant
	private double d;
	
	public Diffusion(Geometry geometry, boolean useBoundaries, double d) {
		super(geometry, useBoundaries);
		this.d = d;
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
	 * in fact set to prevent d from exceeding some maximum value.)
	 * 
	 */
	public void init() {
		double n = (double) dimension();
		double m = (double) connectivity();

        //System.out.println("mode = " + mode());
		// Calculate d. See method javadoc above.
		double a = d * (n / m);
        /*System.out.println("d = " + d);
        System.out.println("n = " + n);
        System.out.println("m = " + m);
        System.out.println("a = " + a);*/
		// Calculate matrix diagonal entries.
		double self = 1.0 - 2 * m * a;

		for (int j = 0; j < sites.length; j++) {
			Coordinate coord = sites[j];

			// Set the diagonal value
			augment(j, j, self);
			
			// Set each neighbor. For reflecting boundary conditions, one or
			// more neighbors may be the diagonal.
			for (int i : neighbors(coord)) {
                //System.out.println("Trying to augment " + sites[i]);
				// Each of the 2m neighbors get d units of solute.
				augment(i, j, a);
			}
		}
	}
	
	public double getD() {
		return d;
	}

}
