/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package continuum.solvers;

import geometry.Geometry;
import no.uib.cipr.matrix.Vector;

public abstract class IntegrationSolver extends Solver {

    public IntegrationSolver(Geometry geometry) {
        super(geometry);
    }

    public abstract Vector solve(Vector input, double dt);
}
