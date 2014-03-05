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
import structural.MatrixUtils;
import structural.postprocess.SolutionViewer;

public abstract class EquilibriumSolver extends Solver {

    public EquilibriumSolver(Geometry geometry) {
        super(geometry);
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
