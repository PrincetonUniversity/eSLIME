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

package io.visual.glyph;

import control.identifiers.Coordinate;

import java.awt.*;

/**
 * Created by dbborens on 4/3/14.
 */
public class CrosshairsGlyph extends Glyph {

    private int radius;
    private int cross;
    private Color color;
    private double circleSize;
    private double crossSize;

    /**
     * @param color      The color of the crosshairs.
     * @param circleSize The relative size of the circle, specified as a multiple of the edge size.
     * @param crossSize  The relative size of the cross, specified as a multiple of the edge size.
     */
    public CrosshairsGlyph(Color color, double circleSize, double crossSize) {
        this.color = color;
        this.circleSize = circleSize;
        this.crossSize = crossSize;
    }

    @Override
    protected void internalInit() {
        radius = calcProportionalSize(circleSize);
        cross = calcProportionalSize(circleSize * crossSize * 2);
    }

    @Override
    public void overlay(Coordinate target) {
        // First, get the center of the cell (in pixels).
        Coordinate center = translator.indexToPixels(target);

        graphics.setColor(color);

        drawCircle(center);
        drawHorizontalLine(center);
        drawVerticalLine(center);
    }

    private void drawHorizontalLine(Coordinate center) {
        int x = center.x() - (cross / 2);
        int y = center.y();

        graphics.drawLine(x, y, x + cross, y);
    }

    private void drawVerticalLine(Coordinate center) {
        int y = center.y() - (cross / 2);
        int x = center.x();

        graphics.drawLine(x, y, x, y + cross);
    }

    private void drawCircle(Coordinate center) {
        int x = center.x() - radius;
        int y = center.y() - radius;

        graphics.drawOval(x, y, radius * 2, radius * 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CrosshairsGlyph)) {
            return false;
        }

        CrosshairsGlyph other = (CrosshairsGlyph) obj;
        if (radius != other.radius) {
            return false;
        }

        if (!color.equals(other.color)) {
            return false;
        }

        return true;
    }
}

