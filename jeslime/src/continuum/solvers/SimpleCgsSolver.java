package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.CGS;
import no.uib.cipr.matrix.sparse.DiagonalPreconditioner;
import no.uib.cipr.matrix.sparse.IterativeSolver;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.sparse.Preconditioner;
import structural.MatrixUtils;
import structural.postprocess.SolutionViewer;

/**
 * Single-grid, conjugate gradient stabilized solver.
 * 
 * @author dbborens
 */
public class SimpleCgsSolver extends EquilibriumSolver {

	public SimpleCgsSolver(Matrix operator, Geometry geometry) {
		super(operator, geometry);
	}

	@Override
	public SolutionViewer solve(Vector source) {
		int n = operator.numRows();

		Vector template = source.copy();
		
		IterativeSolver solver = new CGS(template);
		Preconditioner preconditioner = new DiagonalPreconditioner(n);
        //System.out.println(MatrixUtils.matrixForm(operator));
        //System.exit(0);
		preconditioner.setMatrix(operator);

		DenseVector sol = new DenseVector(n);

		try {
			solver.solve(operator, source, sol);
		} catch (IterativeSolverNotConvergedException ex) {
			ex.printStackTrace();
		}

		return new SolutionViewer(sol, geometry);
		
	}

	@Override
	public SolutionViewer solve(Boolean[] input) {
		int n = operator.numRows();
		
		Vector toSolve = new DenseVector(n);
		for (int i = 0; i < n; i++) {
			if (input[i]) {
				toSolve.set(i, 1.0);
			}
		}
		return solve(toSolve);
	}

}
