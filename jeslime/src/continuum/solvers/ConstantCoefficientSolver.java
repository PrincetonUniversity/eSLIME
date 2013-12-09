package continuum.solvers;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public abstract class ConstantCoefficientSolver extends Solver{

    public ConstantCoefficientSolver(Matrix operator) {
        super(operator);
    }

    public abstract Vector solve(Boolean[] input);
}
