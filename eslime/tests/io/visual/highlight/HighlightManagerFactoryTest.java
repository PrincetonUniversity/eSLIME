/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.highlight;

import factory.io.visual.highlight.HighlightManagerFactory;
import io.visual.glyph.MockGlyph;
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class HighlightManagerFactoryTest extends EslimeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("HighlightManagerFactoryTest.xml");
    }

    public void testPopulate() throws Exception {
        Element highlights = root.element("highlights");

        HighlightManager expected = buildExpected();
        HighlightManager actual = HighlightManagerFactory.instantiate(highlights);

        assertEquals(expected, actual);
    }

    /**
     * If there is no highlight tag, we should get back an empty highlight
     * manager and no exceptions should be thrown.
     *
     * @throws Exception
     */
    public void testNullCase() throws Exception {
        HighlightManager expected = new HighlightManager();
        HighlightManager actual = HighlightManagerFactory.instantiate(null);
        assertEquals(expected, actual);
    }
    private HighlightManager buildExpected() {
        HighlightManager expected = new HighlightManager();
        expected.setGlyph(0, new MockGlyph());
        expected.setGlyph(3, new MockGlyph());
        return expected;
    }
}
