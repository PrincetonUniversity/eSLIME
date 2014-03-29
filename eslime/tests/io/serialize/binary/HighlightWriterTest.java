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

import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;
import structural.utilities.FileConventions;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 3/28/14.
 */
public class HighlightWriterTest extends EslimeLatticeTestCase {

    private int channel = 0;

    public void testLifeCycle() throws Exception {
        runLifeCycle();
        checkFiles();
    }

    private void checkFiles() {
        String filename = FileConventions.makeHighlightFilename(channel);
        assertBinaryFilesEqual(filename);
    }

    private void runLifeCycle() {
        MockGeneralParameters p = makeMockGeneralParameters();
        HighlightWriter query = new HighlightWriter(p);
        query.init(layerManager);
        Coordinate[] highlights = new Coordinate[]{x, y};
        query.step(highlights, 0.1, 2);
        query.dispatchHalt(null);
    }
}
