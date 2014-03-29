/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Vector;
import structural.postprocess.SolutionViewer;
import structural.utilities.EpsilonUtil;

public abstract class ConstantCoefficientSolver extends EquilibriumSolver {

    double epsilon;

    public ConstantCoefficientSolver(Geometry geometry) {
        super(geometry);
        epsilon = EpsilonUtil.epsilon();
    }

    @Override
    public SolutionViewer solve(Vector input) {
        Boolean[] alias = new Boolean[input.size()];
        for (int i = 0; i < input.size(); i++) {
            double value = input.get(i);
            if (value < 0) {
                throw new IllegalStateException("Attempting to alias negative source to boolean. CCS expects positive or zero source values.");
            } else if (value > epsilon) {
                alias[i] = true;
            } else {
                alias[i] = false;
            }
        }

        return solve(alias);
    }

    public abstract SolutionViewer solve(Boolean[] input);
}
