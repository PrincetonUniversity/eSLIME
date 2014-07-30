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

package io.visual.highlight;

import io.visual.glyph.MockGlyph;
import layers.MockSystemState;
import test.EslimeLatticeTestCase;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/2/14.
 */
public class HighlightManagerTest extends EslimeLatticeTestCase {

    MockGlyph glyph;
    HighlightManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        glyph = new MockGlyph();
        query = new HighlightManager();
        query.setGlyph(0, glyph);
    }

    public void testSetGraphics() throws Exception {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        query.setGraphics(graphics);
        assertEquals(graphics, glyph.getGraphics());
    }


    public void testOverlayGlyphs() throws Exception {
        MockSystemState systemState = new MockSystemState();
        systemState.setHighlighted(true);
        query.render(origin, systemState);
        assertEquals(origin, glyph.getLastOverlaid());
    }

    public void testGetHighlightChannels() throws Exception {
        query.setGlyph(2, new MockGlyph());
        int[] expected = new int[]{0, 2};
        int[] actual = query.getHighlightChannels();
        assertArraysEqual(expected, actual, true);
    }
}
