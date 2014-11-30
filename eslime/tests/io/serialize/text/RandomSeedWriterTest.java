/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import processes.StepState;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

public class RandomSeedWriterTest extends EslimeLatticeTestCase {
    public void testLifeCycle() throws Exception {
        makeFiles();
        assertFilesEqual("serializations/random.txt", "random.txt");
    }

    private void makeFiles() throws Exception {
        MockGeneralParameters p = makeMockGeneralParameters();
        p.initializeRandom(1234567890);
        RandomSeedWriter writer = new RandomSeedWriter(p, layerManager);
        writer.init();
        StepState state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(null);
        writer.close();
    }
}