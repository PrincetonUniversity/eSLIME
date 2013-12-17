package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.postprocess.SolutionViewer;

public abstract class ConstantCoefficientSolver extends Solver{

    public ConstantCoefficientSolver(Matrix operator, Geometry geometry) {
        super(operator, geometry);
    }

    public abstract SolutionViewer solve(Boolean[] input);
}
