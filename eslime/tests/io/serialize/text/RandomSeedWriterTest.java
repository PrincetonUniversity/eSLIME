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
        RandomSeedWriter writer = new RandomSeedWriter(p);
        writer.init(layerManager);
        StepState state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);
        writer.dispatchHalt(null);
        writer.close();
    }
}