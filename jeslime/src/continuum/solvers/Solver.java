package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Matrix;

public abstract class Solver {

    protected Matrix operator;
    protected Geometry geometry;

    public Solver(Matrix operator, Geometry geometry) {
        this.geometry = geometry;
        respecify(operator);
    }

    public void respecify(Matrix operator) {
        this.operator = operator;
    }
}
