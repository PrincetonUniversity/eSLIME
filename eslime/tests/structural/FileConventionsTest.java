/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import junit.framework.TestCase;
import structural.utilities.FileConventions;

/**
 * Created by dbborens on 3/26/14.
 */
public class FileConventionsTest extends TestCase {
    public void testMakeContinuumStateFilename() throws Exception {
        String soluteId = "Test";
        String expected = "soluteTest.state.bin";
        String actual = FileConventions.makeContinuumStateFilename(soluteId);
        assertEquals(expected, actual);
    }

    public void testGetMetadataFilename() throws Exception {
        String soluteId = "Test";
        String expected = "soluteTest.metadata.txt";
        String actual = FileConventions.makeContinuumMetadataFilename(soluteId);
        assertEquals(expected, actual);
    }

    public void testMakeHighlightFilename() throws Exception {
        int channel = 0;
        String expected = "channel0.highlight.bin";
        String actual = FileConventions.makeHighlightFilename(channel);
        assertEquals(expected, actual);
    }

    public void testMakeInterfaceFilename() throws Exception {
        int state = 0;
        String expected = "interface_0.txt";
        String actual = FileConventions.makeInterfaceFilename(state);
        assertEquals(expected, actual);
    }
}
