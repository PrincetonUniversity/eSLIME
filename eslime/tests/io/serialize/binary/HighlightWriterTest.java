/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.binary;

import control.identifiers.Coordinate;
import processes.MockStepState;
import structural.MockGeneralParameters;
import structural.utilities.FileConventions;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 3/28/14.
 */
public class HighlightWriterTest extends EslimeLatticeTestCase {


    private int[] channels = new int[]{0, 7};

    public void testLifeCycle() throws Exception {
        runLifeCycle();
        checkFiles();
    }

    private void checkFiles() {
        for (int channel : channels) {
            String filename = FileConventions.makeHighlightFilename(channel);
            assertBinaryFilesEqual(filename);
        }
    }

    private void runLifeCycle() {
        MockGeneralParameters p = makeMockGeneralParameters();
        HighlightWriter query = new HighlightWriter(p, channels, layerManager);
        query.init();
        MockStepState stepState = new MockStepState(0.1, 2);
        stepState.setHighlights(0, new Coordinate[]{x, y});
        stepState.setHighlights(7, new Coordinate[]{origin});
        stepState.record(cellLayer);
        query.flush(stepState);
        query.dispatchHalt(null);
    }

}
