package jeslime.mock;

import geometry.Geometry;
import layers.solute.Extremum;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by dbborens on 12/16/13.
 */
public class MockSoluteLayer extends SoluteLayer {

    public MockSoluteLayer(Integer id) {
        super(null, id);
    }

    private DenseVector state;

    public void setState(DenseVector state) {
        this.state = state;
    }

    @Override
    public DenseVector getState() {
        return super.getState();
    }
}
