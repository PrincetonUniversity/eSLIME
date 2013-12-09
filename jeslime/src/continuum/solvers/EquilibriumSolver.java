package continuum.solvers;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public abstract class EquilibriumSolver extends ConstantCoefficientSolver {

    public EquilibriumSolver(Matrix operator) {
        super(operator);
    }

    public abstract Vector solve(Vector input);

}
