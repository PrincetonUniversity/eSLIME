/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.Vector;

/**
 * Created by dbborens on 12/26/14.
 */
public class ContinuumLayerContent {
    protected Vector state;

    public Vector getState() {
        return state;
    }

    public void setState(Vector state) {
        this.state = state;
    }

    public void reset() {
        this.state = null;
    }
}
