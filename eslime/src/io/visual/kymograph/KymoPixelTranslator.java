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

package io.visual.kymograph;

import control.identifiers.Coordinate;
import io.visual.VisualizationProperties;
import io.visual.map.PixelTranslator;

import java.awt.*;

/**
 *
 * A 2D rectangular plot with X axis as time,
 * and Y axis as a 1-dimensional system state.
 *
 * Created by dbborens on 5/20/14.
 */
public class KymoPixelTranslator extends PixelTranslator {

    @Override
    protected void calcLimits(VisualizationProperties properties) {
        int xMin, xMax, yMin, yMax;

        xMin = 2147483647;
        xMax = -2147483648;

        yMin = 2147483647;
        yMax = -2147483648;

        for (Coordinate c : properties.getCoordinates()) {
            int x = c.x();
            int y = c.y();

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

        if ((xMin != 0) || (xMax != 0)) {
            throw new IllegalArgumentException("Received a non-zero x " +
                    "coordinate in kymograph. Kymograph requires a 1D system.");
        }

        int dy = (int) edge * (yMax - yMin + 1);

        int n = properties.getFrames().length;
        int lastFrame = properties.getFrames()[n - 1];
        int dt = (int) edge * (lastFrame + 1);
        imageDims = new Coordinate(dt, dy, 0);
    }

    @Override
    protected void calcOrigin() {
        int t = (int) Math.round(edge / 2);
        int y = (int) Math.round(edge / 2);
        origin = new Coordinate(t, y, 0);

    }

    @Override
    public Coordinate resolve(Coordinate c, int frame, double time) {
        int x = frame;
        int y = c.y();

        int xPixels = (int) Math.round(x * edge);
        int yPixels = (int) Math.round(y * edge);

        Coordinate center = new Coordinate(xPixels, yPixels, 0);

        int px = origin.x() + center.x();
        int py = imageDims.y() - origin.y() - center.y();

        Coordinate ret = new Coordinate(px, py, 0);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof KymoPixelTranslator);
    }

    @Override
    public Polygon makePolygon(Coordinate c, int frame, double time) {
        Coordinate centerPx = resolve(c, frame, time);
        Polygon p = new Polygon();
        int d = (int) Math.round(edge / 2);

        p.addPoint(centerPx.x() - d, centerPx.y() - d);
        p.addPoint(centerPx.x() + d, centerPx.y() - d);
        p.addPoint(centerPx.x() + d, centerPx.y() + d);
        p.addPoint(centerPx.x() - d, centerPx.y() + d);

        return p;
    }

    @Override
    public double getDiagonal() {
        return Math.sqrt(2) * edge;
    }
}
