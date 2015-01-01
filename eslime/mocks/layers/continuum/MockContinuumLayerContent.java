/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import no.uib.cipr.matrix.Vector;

import java.util.function.Function;

/**
 * Created by dbborens on 12/28/14.
 */
public class MockContinuumLayerContent extends ContinuumLayerContent {

    private boolean triggered = false;
    private boolean reset = false;

    public MockContinuumLayerContent(Function<Coordinate, Integer> indexer) {
        super(indexer);
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    @Override
    public void setState(Vector state) {
        super.setState(state);
        triggered = true;
    }

    @Override
    public void reset() {
        super.reset();
        reset = true;
    }

    public boolean isReset() {
        return reset;
    }
}
