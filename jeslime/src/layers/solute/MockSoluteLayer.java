package layers.solute;

import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;

/**
 * Created by dbborens on 12/16/13.
 */
public class MockSoluteLayer extends SoluteLayer {

    public MockSoluteLayer(String id) {
        super(null, id);
    }

    private DenseVector state;

    public void setState(DenseVector state) {
        this.state = state;
    }

    @Override
    public DenseVector getState() {
        return state;
    }
}
