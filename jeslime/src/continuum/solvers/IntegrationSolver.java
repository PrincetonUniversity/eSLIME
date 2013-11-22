package continuum.solvers;

import no.uib.cipr.matrix.Vector;

public abstract class IntegrationSolver extends Solver {

	public abstract Vector solve(Vector input, double dt);
}
