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
import no.uib.cipr.matrix.Matrix;
import structural.utilities.MatrixUtils;

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
