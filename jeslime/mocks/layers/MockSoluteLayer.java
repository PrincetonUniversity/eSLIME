package layers;

import continuum.solvers.Solver;
import geometry.Geometry;
import geometry.Geometry;
import layers.solute.SoluteLayer;
import structural.postprocess.SolutionViewer;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class MockSoluteLayer extends SoluteLayer {
    public MockSoluteLayer() {
        super(null, null, null, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    @Override
    public Solver getSolver() {
        return null;
    }

    @Override
    public void setDt(double dt) {

    }

    @Override
    public void integrate() {

    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    private Geometry geometry;

    @Override
    public void push(SolutionViewer state) {
        super.push(state);
    }
}
