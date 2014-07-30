/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.binary;

import processes.MockStepState;
import structural.MockGeneralParameters;
import structural.utilities.FileConventions;
import test.EslimeTestCase;

/**
 * Created by dbborens on 3/28/14.
 */
public class TimeWriterTest extends EslimeTestCase {

    public void testLifeCycle() throws Exception {
        // Run through a life cycle, creating output in the process
        generateFile();

        // There should now be an output file to match the fixture named after
        // the time file naming convention
        String filename = FileConventions.TIME_FILENAME;

        // Assert the files are equal
        assertBinaryFilesEqual(filename);
    }

    private void generateFile() {
        MockGeneralParameters p = makeMockGeneralParameters();
        TimeWriter query = new TimeWriter(p);
        query.init(null);
//        query.step(null, 0.5, 2);
//        query.step(null, 1.3, 4);
        query.flush(new MockStepState(0.5, 2));
        query.flush(new MockStepState(1.3, 4));
        query.dispatchHalt(null);
    }
}
