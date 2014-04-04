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

package io.visual.glyph;

import org.dom4j.Element;
import test.EslimeTestCase;

import java.awt.*;

/**
 * Created by dbborens on 4/4/14.
 */
public class GlyphFactoryTest extends EslimeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/GlyphFactoryTest.xml");
    }

    public void testMockCase() throws Exception {
        Element e = root.element("mock-case");
        Glyph actual = GlyphFactory.instantiate(e);
        Glyph expected = new MockGlyph();

        assertEquals(expected, actual);
    }

    public void testDotCompleteCase() throws Exception {
        Element e = root.element("dot-case-complete");
        Glyph actual = GlyphFactory.instantiate(e);

        Color color = Color.decode("0xFF00AA");
        double size = 0.4;
        Glyph expected = new DotGlyph(color, size);

        assertEquals(expected, actual);
    }

    // We test one class of glyph for default handling
    public void testDotDefaultCase() throws Exception {
        Element e = root.element("dot-case-default");
        Glyph actual = GlyphFactory.instantiate(e);

        Glyph expected = new DotGlyph(GlyphFactory.DEFAULT_COLOR,
                GlyphFactory.DEFAULT_SIZE);

        assertEquals(expected, actual);
    }
}
