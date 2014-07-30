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
