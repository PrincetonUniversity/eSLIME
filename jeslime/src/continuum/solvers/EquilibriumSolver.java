package continuum.solvers;

import no.uib.cipr.matrix.Vector;

public abstract class EquilibriumSolver extends ConstantCoefficientSolver {

	public abstract Vector solve(Vector input);

}
