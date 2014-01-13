package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Matrix;
import structural.MatrixUtils;

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

    @Override
    /**
     * Include an overloaded equality operation for solvers,
     * to be used in testing. This should be overloaded as
     * necessary for complete coverage.
     */
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Solver other = (Solver) obj;

        // Check geometry
        if (!other.geometry.equals(this.geometry)) {
            return false;
        }

        // Check operator
        if (!MatrixUtils.equal(this.operator, other.operator)) {
            return false;
        }

        // If class, operator and geometry are all equal, then they are
        // equal as far as the base class is concerned
        return true;
    }

}
