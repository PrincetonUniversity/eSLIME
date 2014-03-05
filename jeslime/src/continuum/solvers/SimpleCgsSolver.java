/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

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
