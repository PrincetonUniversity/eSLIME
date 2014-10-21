/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
        } else if (d == 3 && c == 3) {
            return new CubePixelTranslator();

        } else if (d == 1 && c == 1)  {
            return new RectPixelTranslator();

        } else {
            throw new IllegalArgumentException("Unrecognized geometry " +
                    "(dimensionality = " + d + "; connectivity = " + c + ")");
        }
    }
}
