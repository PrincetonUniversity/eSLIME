/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import processes.BaseProcessArguments;
import processes.MockStepState;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/24/14.
 */
public class RecordTest extends EslimeLatticeTestCase {

    public void testLifeCycle() throws Exception {
        MockStepState stepState = new MockStepState();
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, null);
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);
        Record query = new Record(arguments, cpArguments);
        query.target(null);
        query.fire(stepState);
        assertTrue(stepState.isRecorded());
    }
}
