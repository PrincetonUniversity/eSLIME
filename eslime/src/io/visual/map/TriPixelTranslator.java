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

import control.identifiers.Coordinate;
import io.visual.VisualizationProperties;

import java.awt.*;

/**
 * Created by dbborens on 4/1/14.
 */
public class TriPixelTranslator extends PixelTranslator {

    private int yOffset;

    @Override
    public double getDiagonal() {
        return 2 * edge;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TriPixelTranslator);
    }

    protected Coordinate indexToPixels(Coordinate c) {
        int x = c.x();
        int y = c.y();

        Coordinate center = indexToOffset(x, y);

        int px = origin.x() + center.x();
        int py = imageDims.y() - origin.y() - center.y();

        Coordinate ret = new Coordinate(px, py, 0);
        return ret;
    }

    @Override
    public Coordinate resolve(Coordinate c, int frame, double time) {
        return indexToPixels(c);
    }

    @Override
    protected void calcLimits(VisualizationProperties mapState) {
        int xMin, xMax, yMin, yMax;

        xMin = 2147483647;
        xMax = -2147483648;

        yMin = 2147483647;
        yMax = -2147483648;

        for (Coordinate c : mapState.getCoordinates()) {
            Coordinate pixels = indexToOffset(c.x(), c.y());
            int x = pixels.x();
            int y = pixels.y();

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

        int dy = (int) Math.ceil((yMax - yMin) + (Math.sqrt(3.0) * edge));
        int dx = (int) Math.ceil(xMax - xMin + (2 * edge)) + 1;
        imageDims = new Coordinate(dx, dy, 0);
        calcYOffset(yMin);
    }

    private void calcYOffset(int y) {
        int y0 = (int) (indexToOffset(0, 0).y() - (0.5 * Math.sqrt(3.0) * edge));
        yOffset = y - y0;
    }

    @Override
    protected void calcOrigin() {
        int x = (int) Math.round(edge);
        int y = (int) Math.round(edge * Math.sqrt(3.0f)) - yOffset;
        origin = new Coordinate(x, y, 0);
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

    public Polygon makePolygon(Coordinate coord, int frame, double time) {
        Coordinate centerPx = resolve(coord, frame, time);
        Polygon p = new Polygon();
        for (double theta = 0d; theta < 360d; theta += 60d) {
            double rad = (theta / 180) * Math.PI;
            int x1 = (int) Math.round(centerPx.x() + edge * Math.cos(rad));
            int y1 = (int) Math.round(centerPx.y() + edge * Math.sin(rad));
            p.addPoint(x1, y1);
        }

        return p;
    }

}
