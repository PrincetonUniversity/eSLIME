package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.MatrixUtils;
import structural.postprocess.SolutionViewer;

public abstract class EquilibriumSolver extends ConstantCoefficientSolver {

    public EquilibriumSolver(Matrix operator, Geometry geometry) {
        super(operator, geometry);
    }

    public abstract SolutionViewer solve(Vector input);

    @Override
    /**
     * Respecifies the solver to the matrix I - A, where I is the identity
     * matrix and A is the operator to be integrated to infinity.
     *
     * The rationale is as follows:
     *
     *    c' = A c + g
     *
     * Where c' is c at the next time and g is the source vector.
     * Therefore, at steady state, we have
     *
     *    c = A c + g
     *
     * i.e.,
     *
     *    g = (I - A) c
     *
     */
    public void respecify(Matrix original) {
        int n = original.numColumns();
        Matrix identity = MatrixUtils.I(n);

        if (MatrixUtils.equal(original, identity)) {
            super.respecify(original);
        } else {
            Matrix comp = original.copy();

            comp.scale(-1.0);
            comp.add(identity);

            super.respecify(comp);
        }
    }
}
