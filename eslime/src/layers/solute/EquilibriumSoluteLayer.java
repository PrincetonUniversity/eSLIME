/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.solute;

import continuum.solvers.EquilibriumSolver;
import geometry.Geometry;
import layers.LayerManager;

/**
 * Created by dbborens on 1/6/14.
 */
public class EquilibriumSoluteLayer extends SoluteLayer {
    private EquilibriumSolver solver;

    public EquilibriumSoluteLayer(Geometry geom, LayerManager manager, EquilibriumSolver solver, String id) {
        super(geom, manager, solver, id);
    }

    @Override
    public void setDt(double dt) {
        throw new UnsupportedOperationException();
    }

    public void integrate() {
        // Check sources.
        state = solver.solve(source);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EquilibriumSoluteLayer that = (EquilibriumSoluteLayer) o;

        if (solver != null ? !solver.equals(that.solver) : that.solver != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return solver != null ? solver.hashCode() : 0;
    }
}
