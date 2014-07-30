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

package io.visual.map;

import geometry.Geometry;

/**
 * Created by dbborens on 4/3/14.
 */
public class PixelTranslatorFactory {
    public static PixelTranslator instantiate(Geometry geometry) {
        int d = geometry.getDimensionality();
        int c = geometry.getConnectivity();

        // Rectangular lattice
        if (d == 2 && c == 2) {
            return new RectPixelTranslator();

            // Triangular lattice
        } else if (d == 2 && c == 3) {
            return new TriPixelTranslator();

            // Cubic lattide
        } else if (d == 2 && c == 3) {
            throw new UnsupportedOperationException("Support for cubic lattice not yet implemented. (Hint: it should consist of a grid of rectangular grids.");

        } else if (d == 1 && c == 1)  {
            return new RectPixelTranslator();

        } else {
            throw new IllegalArgumentException("Unrecognized geometry " +
                    "(dimensionality = " + d + "; connectivity = " + c + ")");
        }
    }
}
