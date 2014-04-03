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
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class HighlightFactoryTest extends EslimeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("HighlightFactoryTest.xml");
    }

    public void testPopulate() throws Exception {
        Element highlights = root.element("highlights");

        HighlightManager expected = buildExpected();
        HighlightManager actual = HighlightFactory.instantiate(highlights);

        assertEquals(expected, actual);
    }

    private HighlightManager buildExpected() {
        HighlightManager expected = new HighlightManager();
        expected.setGlyph(0, new MockGlyph());
        expected.setGlyph(3, new MockGlyph());
        return expected;
    }
}
