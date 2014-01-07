package layers.solute;

import continuum.solvers.Solver;
import geometry.Geometry;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;

/**
 * Created by dbborens on 12/16/13.
 */
public class MockSoluteLayer extends SoluteLayer {

    @Override
    public Solver getSolver() {
        return null;
    }

    public MockSoluteLayer(String id) {
        super(null, null, id);
    }

    private DenseVector state;

    public void setState(DenseVector state) {
        this.state = state;
    }

    @Override
    public DenseVector getState() {
        return state;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
