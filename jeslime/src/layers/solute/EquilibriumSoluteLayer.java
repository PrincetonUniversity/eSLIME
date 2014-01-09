package layers.solute;

import continuum.solvers.EquilibriumSolver;
import continuum.solvers.Solver;
import geometry.Geometry;
import layers.LayerManager;
import no.uib.cipr.matrix.Vector;
import sun.tools.jar.resources.jar_de;

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
}
