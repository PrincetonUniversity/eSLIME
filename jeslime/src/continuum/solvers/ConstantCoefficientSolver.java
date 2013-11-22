package continuum.solvers;

import no.uib.cipr.matrix.Vector;

public abstract class ConstantCoefficientSolver extends Solver{

	public abstract Vector solve(Boolean[] input);
}
