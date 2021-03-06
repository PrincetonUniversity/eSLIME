/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory.io.visual.color;

import control.arguments.Argument;
import control.arguments.ConstantDouble;
import factory.io.visual.color.SurfaceGrowthColorManagerFactory;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.color.SurfaceGrowthColorManager;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class SurfaceGrowthColorManagerFactoryTest extends EslimeTestCase {
    private Element root;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        root = readXmlFile("factories/io/visual/color/SurfaceGrowthColorManagerFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    public void testImplicit() throws Exception {
        Element e = root.element("implicit-case");
        SurfaceGrowthColorManager actual = SurfaceGrowthColorManagerFactory.instantiate(e, p);

        ColorManager base = new DefaultColorManager();
        Argument<Double> saturationScaling = new ConstantDouble(SurfaceGrowthColorManagerFactory.DEFAULT_SATURATION_SCALING);
        Argument<Double> luminanceScaling = new ConstantDouble(SurfaceGrowthColorManagerFactory.DEFAULT_LUMINANCE_SCALING);
        SurfaceGrowthColorManager expected = new SurfaceGrowthColorManager(base, luminanceScaling, saturationScaling);

        assertEquals(expected, actual);
    }

    public void testExplicit() throws Exception {
        Element e = root.element("explicit-case");
        SurfaceGrowthColorManager actual = SurfaceGrowthColorManagerFactory.instantiate(e, p);

        ColorManager base = new DefaultColorManager();
        Argument<Double> saturationScaling = new ConstantDouble(0.75);
        Argument<Double> luminanceScaling = new ConstantDouble(0.25);
        SurfaceGrowthColorManager expected = new SurfaceGrowthColorManager(base, luminanceScaling, saturationScaling);

        assertEquals(expected, actual);
    }
}