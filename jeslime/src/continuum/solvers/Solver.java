package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Matrix;

public abstract class Solver {

    protected Matrix operator;
    protected Geometry geometry;

    public Solver(Geometry geometry) {
        this.geometry = geometry;
    }

    public void respecify(Matrix operator) {
        this.operator = operator;
    }

    public Matrix getOperator() {
        return operator.copy();
    }
}
