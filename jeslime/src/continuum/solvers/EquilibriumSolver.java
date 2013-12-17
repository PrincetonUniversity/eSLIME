package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.postprocess.SolutionViewer;

public abstract class EquilibriumSolver extends ConstantCoefficientSolver {

    public EquilibriumSolver(Matrix operator, Geometry geometry) {
        super(operator, geometry);
    }

    public abstract SolutionViewer solve(Vector input);

}
