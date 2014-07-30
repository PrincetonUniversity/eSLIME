/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import geometry.set.CompleteSet;
import processes.MockStepState;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/24/14.
 */
public class RecordTest extends EslimeLatticeTestCase {

    public void testLifeCycle() throws Exception {
        MockStepState stepState = new MockStepState();
        Record query = new Record(null, layerManager, new CompleteSet(geom), 0, null);
        query.target(null);
        query.fire(stepState);
        assertTrue(stepState.isRecorded());
    }
}
