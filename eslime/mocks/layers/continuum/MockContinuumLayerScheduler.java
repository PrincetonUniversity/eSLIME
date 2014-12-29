/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

/**
 * Created by dbborens on 12/28/14.
 */
public class MockContinuumLayerScheduler extends ContinuumLayerScheduler {

    private boolean reset;

    public MockContinuumLayerScheduler() {
        super(null, null);
        reset = false;
    }

    @Override
    public void reset() {
        reset = true;
    }

    public boolean isReset() {
        return reset;
    }
}
