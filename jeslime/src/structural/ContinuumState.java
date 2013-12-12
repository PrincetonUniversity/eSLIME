package structural;

import no.uib.cipr.matrix.DenseVector;

/**
 * Created by dbborens on 12/11/13.
 */
public class ContinuumState {

    private final int frame;
    private DenseVector data;
    private double gillespie;

    public ContinuumState(DenseVector data, double gillespie, int frame) {
        this.data = data;
        this.gillespie = gillespie;
        this.frame = frame;
    }
    public int getFrame() {
        return frame;
    }

    public double getGillespie() {
        return gillespie;
    }

    public DenseVector getData() {
        return data;
    }
}
