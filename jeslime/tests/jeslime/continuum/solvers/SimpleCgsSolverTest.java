package jeslime.continuum.solvers;

import continuum.solvers.SimpleCgsSolver;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;

public class SimpleCgsSolverTest extends EquilibriumSolverTest {

    public void constructSolver(Matrix operator) {
        solver = new SimpleCgsSolver(operator, geometry);
    }
}
