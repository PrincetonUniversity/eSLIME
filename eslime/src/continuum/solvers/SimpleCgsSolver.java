/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.*;
import structural.postprocess.SolutionViewer;

/**
 * Single-grid, conjugate gradient stabilized solver.
 *
 * @author dbborens
 */
public class SimpleCgsSolver extends ConstantCoefficientSolver {

    public SimpleCgsSolver(Geometry geometry) {
        super(geometry);
    }

    @Override
    public SolutionViewer solve(Vector source) {
        int n = operator.numRows();

        Vector template = source.copy();

        IterativeSolver solver = new CGS(template);
        Preconditioner preconditioner = new DiagonalPreconditioner(n);
        preconditioner.setMatrix(operator);

        DenseVector sol = new DenseVector(n);

        try {
            solver.solve(operator, source, sol);
        } catch (IterativeSolverNotConvergedException ex) {
            ex.printStackTrace();
        }

        return new SolutionViewer(sol, geometry);

    }

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
