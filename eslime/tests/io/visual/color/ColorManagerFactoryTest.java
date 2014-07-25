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

import control.arguments.Argument;
import control.arguments.ConstantDouble;
import factory.io.visual.color.ColorManagerFactory;
import factory.io.visual.color.SurfaceGrowthColorManagerFactory;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/2/14.
 */
public class ColorManagerFactoryTest extends EslimeTestCase {
    private Element fixtureRoot;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("ColorManagerFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    public void testDefaultColorCase() {
        Element element = fixtureRoot.element("default-case");
        ColorManager actual = ColorManagerFactory.instantiate(element, p);
        ColorManager expected = new DefaultColorManager();
        assertEquals(expected, actual);
    }

    public void testSurfaceGrowthCase() {
        Element element = fixtureRoot.element("surface-growth-case");

        ColorManager base = new DefaultColorManager();
        Argument<Double> saturationScaling = new ConstantDouble(SurfaceGrowthColorManagerFactory.DEFAULT_SATURATION_SCALING);
        Argument<Double> luminanceScaling = new ConstantDouble(SurfaceGrowthColorManagerFactory.DEFAULT_LUMINANCE_SCALING);
        SurfaceGrowthColorManager expected = new SurfaceGrowthColorManager(base, luminanceScaling, saturationScaling);

        ColorManager actual = ColorManagerFactory.instantiate(element, p);

        assertEquals(expected, actual);
    }
    public void testNullCase() {
        ColorManager actual = ColorManagerFactory.instantiate(null, p);
        ColorManager expected = new DefaultColorManager();
        assertEquals(expected, actual);
    }
}
