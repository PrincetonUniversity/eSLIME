/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import factory.io.visual.color.ColorFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

import java.awt.*;

/**
 * Created by dbborens on 4/4/14.
 */
public class ColorFactoryTest extends EslimeTestCase {

    Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        root = readXmlFile("factories/ColorFactoryTest.xml");
    }

    public void testDefaultCase() throws Exception {
        Element e = null;
        Color actual = ColorFactory.instantiate(e, Color.WHITE);
        Color expected = Color.WHITE;
        assertEquals(expected, actual);
    }

    public void testHexCase() throws Exception {
        Element e = root.element("hex-case");
        Color actual = ColorFactory.instantiate(e, null);
        Color expected = Color.decode("0xAABBCC");
        assertEquals(expected, actual);
    }
}

