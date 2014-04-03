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

package io.visual.color;

import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/2/14.
 */
public class ColorManagerFactoryTest extends EslimeTestCase {
    Element fixtureRoot;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("ColorManagerFactoryTest.xml");
    }

    public void testDefaultColorCase() {
        Element element = fixtureRoot.element("default-case");
        ColorManager actual = ColorManagerFactory.instantiate(element);
        ColorManager expected = new DefaultColorManager();
        assertEquals(expected, actual);
    }

    public void testNullCase() {
        ColorManager actual = ColorManagerFactory.instantiate(null);
        ColorManager expected = new DefaultColorManager();
        assertEquals(expected, actual);
    }
}
