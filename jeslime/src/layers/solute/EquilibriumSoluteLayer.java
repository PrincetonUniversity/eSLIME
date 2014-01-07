package layers.solute;

import continuum.solvers.EquilibriumSolver;
import continuum.solvers.Solver;
import geometry.Geometry;
import layers.LayerManager;
import no.uib.cipr.matrix.Vector;

/**
 * Created by dbborens on 1/6/14.
 */
public class EquilibriumSoluteLayer extends SoluteLayer {
    private EquilibriumSolver solver;

    public EquilibriumSoluteLayer(Geometry geom, LayerManager manager, EquilibriumSolver solver, String id) {
        super(geom, manager, id);
        this.solver = solver;
    }

    public void setSourceVector(Vector sourceVector) {

    }

    public void integrate() {
        // Check sources.

        // Integrate forward.
    }

    @Override
    public Solver getSolver() {
        return null;
    }
}
