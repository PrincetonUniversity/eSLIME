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

import structural.identifiers.Coordinate;

import java.awt.*;

/**
 * Manages the translation from coordinates to pixels in map visualizations.
 * <p/>
 * Created by dbborens on 4/1/14.
 */
public abstract class PixelTranslator {

    // Dimensions of the the lattice, as measured from the lower-left-most cell
    // to the upper-right-most cell (in units of cells)
    protected Coordinate latticeDims;

    // Dimensions of the image (in units of pixels)
    protected Coordinate imageDims;

    // Lower left coordinate, in pixel space
    protected Coordinate origin;

    protected double edge;

    /**
     * Load required state from the map and construct coordinate to pixel
     * translations.
     *
     * @param mapState
     */
    public void init(MapState mapState) {
        edge = mapState.getEdge();
        Coordinate[] coordinates = mapState.getCoordinates();
        mapState.setCoordinates(coordinates);
        int[] extrema = calcModelExtrema(mapState);
        calcLimits(extrema);
        calcOrigin();
    }

    private int[] calcModelExtrema(MapState mapState) {
        // Find limits of coordinate range
        Coordinate[] sites = mapState.getCoordinates();

        int xMin, xMax, yMin, yMax;

        xMin = 2147483647;
        xMax = -2147483648;

        yMin = 2147483647;
        yMax = -2147483648;

        for (int i = 0; i < sites.length; i++) {
            Coordinate coord = sites[i];

            int x = coord.x();
            int y = coord.y();

            if (x < xMin) {
                xMin = x;
            }

            if (x > xMax) {
                xMax = x;
            }

            if (y < yMin) {
                yMin = y;
            }

            if (y > yMax) {
                yMax = y;
            }
        }

        // We expect only positive or zero coordinates.
        if ((xMin != 0) || (yMin != 0)) {
            throw new IllegalStateException("Negative coordinate or no coordinates loaded.");
        }

        return new int[]{xMin, xMax, yMin, yMax};
    }

    protected abstract void calcLimits(int[] extrema);

    protected abstract void calcOrigin();

    /**
     * Convert coordinate (in the cell-based coordinate system of the model)
     * to the pixel coordinate of the center of the coordinate.
     *
     * @param c the coordinate to be converted.
     * @return
     */
    public abstract Coordinate indexToPixels(Coordinate c);

    public Coordinate getImageDims() {
        return imageDims;
    }

    @Override
    public abstract boolean equals(Object obj);

    public abstract Polygon makePolygon(Coordinate c);

    /**
     * Return the length of the diagonal of a polygon, based on the geometry
     * of the lattice and the length of the edges.
     */
    public abstract double getDiagonal();

//    /**
//     * Convert coordinate (in the cell-based coordinate system of the model)
//     * to the pixel coordinate of the center of the hexagon.
//     *
//     * @param c the coordinate to be converted.
//     * @return
//     */
//    public abstract Coordinate indexToCenterPixels(Coordinate c);
}
