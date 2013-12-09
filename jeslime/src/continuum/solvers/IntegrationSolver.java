package continuum.solvers;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public abstract class IntegrationSolver extends Solver {

    public IntegrationSolver(Matrix operator) {
        super(operator);
    }
	public abstract Vector solve(Vector input, double dt);
}
