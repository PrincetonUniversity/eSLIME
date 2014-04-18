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

import control.identifiers.Coordinate;

import java.awt.*;

/**
 * Manages the translation from coordinates to pixels in map visualizations.
 * <p/>
 * Created by dbborens on 4/1/14.
 */
public abstract class PixelTranslator {


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
        calcLimits(mapState);
        calcOrigin();
    }

    protected abstract void calcLimits(MapState mapState);

    // Provide the coordinate of the lower-leftmost coordinate to be included in.
    // the field of view. This may not be a coordinate that exists in this
    // geometry (ie, in the case of non-rectangular geometries).
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
