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

package factory.io.visual.color;

import control.GeneralParameters;
import control.arguments.Argument;
import factory.control.arguments.DoubleArgumentFactory;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.color.SurfaceGrowthColorManager;
import org.dom4j.Element;


/**
 * Created by dbborens on 7/25/14.
 */
public abstract class SurfaceGrowthColorManagerFactory {

    public static double DEFAULT_SATURATION_SCALING = 0.5;
    public static double DEFAULT_LUMINANCE_SCALING = 1.0;

    public static SurfaceGrowthColorManager instantiate(Element e, GeneralParameters p) {
        ColorManager base = instantiateBase(e, p);
        Argument<Double> luminanceScale = DoubleArgumentFactory.instantiate(e, "luminance-scale", DEFAULT_LUMINANCE_SCALING, p.getRandom());
        Argument<Double> saturationScale = DoubleArgumentFactory.instantiate(e, "saturation-scale", DEFAULT_SATURATION_SCALING, p.getRandom());

        SurfaceGrowthColorManager ret = new SurfaceGrowthColorManager(base, luminanceScale, saturationScale);

        return ret;
    }

    private static ColorManager instantiateBase(Element e, GeneralParameters p) {
        Element baseElement = e.element("base");
        if (baseElement == null) {
            return new DefaultColorManager();
        }

        ColorManager base = ColorManagerFactory.instantiate(baseElement, p);
        return base;
    }
}
