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

