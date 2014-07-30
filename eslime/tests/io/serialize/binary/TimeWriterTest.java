/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
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
