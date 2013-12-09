package continuum.solvers;

import no.uib.cipr.matrix.Matrix;

public abstract class Solver {

    protected Matrix operator;

    public Solver(Matrix operator) {
        respecify(operator);
    }

    public void respecify(Matrix operator) {
        this.operator = operator;
    }
}
