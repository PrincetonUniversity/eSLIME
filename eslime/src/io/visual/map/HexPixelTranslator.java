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
 * Created by dbborens on 4/1/14.
 */
public class HexPixelTranslator extends PixelTranslator {
    @Override
    public double getDiagonal() {
        return 2 * edge;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof HexPixelTranslator);
    }

    @Override
    protected void calcLimits(int[] extrema) {
        int xMin = extrema[0];
        int xMax = extrema[1];
        int yMin = extrema[2];
        int yMax = extrema[3];

        // Hexagonal lattice: height value depends on width.
        int mx = xMax - xMin;
        int my = (yMax - yMin) - (mx / 2);
        latticeDims = new Coordinate(mx, my, 0);

        // Calculate pixel bounds.
        double heightFP = Math.sqrt(3.0) * ((double) (my) + 1.5) * edge;
        double widthFP = 1.5 * ((double) (mx) + 1.5f) * edge;

        int px = (int) Math.ceil(widthFP);
        int py = (int) Math.ceil(heightFP);
        imageDims = new Coordinate(px, py, 0);
    }

    @Override
    protected void calcOrigin() {
        int x = (int) Math.round(edge);
        int y = (int) Math.round(edge * Math.sqrt(3.0f));
        origin = new Coordinate(x, y, 0);
    }

    @Override
    public Coordinate indexToPixels(Coordinate c) {
        int x = c.x();
        int y = c.y();

        Coordinate center = indexToOffset(x, y);

        int px = origin.x() + center.x();
        int py = imageDims.y() - origin.y() - center.y();

        Coordinate ret = new Coordinate(px, py, 0);
        return ret;
    }

    private Coordinate indexToOffset(int x, int y) {
        double xOffsetFP = edge * (double) x * 1.5;
        int ox = (int) Math.round(xOffsetFP);

        double yOffsetFP = (-0.5f * Math.sqrt(3.0f) * edge * (double) x)
                + (Math.sqrt(3.0f) * edge * (double) y);
        int oy = (int) Math.round(yOffsetFP);

        Coordinate ret = new Coordinate(ox, oy, 0);
        return (ret);
    }

    public Polygon makePolygon(Coordinate coord) {
        Coordinate centerPx = indexToPixels(coord);
        Polygon p = new Polygon();
        for (double theta = 0d; theta < 360d; theta += 60d) {
            double rad = (theta / 180) * Math.PI;
            int x1 = (int) Math.round(centerPx.x() + edge * Math.cos(rad));
            int y1 = (int) Math.round(centerPx.y() + edge * Math.sin(rad));
            p.addPoint(x1, y1);
        }

        return p;
    }

//    @Override
//    public Coordinate indexToCenterPixels(Coordinate c) {
//        // First, get coordinate of lower left hand corner.
//        Coordinate corner = indexToPixels(c);
//
//        // x distance to center = edge length.
//        int dx = (int) Math.round(edge);
//
//        // y distance to center = sqrt(3)/2 * edge length.
//        int dy = (int) Math.round((Math.sqrt(3.0) / 2.0) * edge);
//
//        int x = corner.x() + dx;
//        int y = corner.y() + dy;
//
//        return new Coordinate(x + dx, y + dy, 0);
//    }
}
