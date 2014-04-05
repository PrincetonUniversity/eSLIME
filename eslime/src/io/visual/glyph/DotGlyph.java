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

import structural.identifiers.Coordinate;

import java.awt.*;

/**
 * Created by dbborens on 4/3/14.
 */
public class DotGlyph extends Glyph {

    private int radius;
    private Color color;
    private double size;

    /**
     * @param color The color of the dot.
     * @param size  The relative size of the dot, specified as a multiple of the edge size.
     */
    public DotGlyph(Color color, double size) {
        this.color = color;
        this.size = size;
    }

    @Override
    protected void internalInit() {
        radius = calcRadius(size);
    }

    private int calcRadius(double size) {
        // The length of a diagonal of the cell polygon.
        double diagonal = translator.getDiagonal();

        double scaled = diagonal * size / 2.0;
        return (int) scaled;
    }

    @Override
    public void overlay(Coordinate target) {
        // First, get the center of the cell (in pixels).
        Coordinate center = translator.indexToPixels(target);

        int x = center.x() - radius;
        int y = center.y() - radius;

        graphics.setColor(color);
        graphics.fillOval(x, y, radius * 2, radius * 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DotGlyph)) {
            return false;
        }

        DotGlyph other = (DotGlyph) obj;
        if (radius != other.radius) {
            return false;
        }

        if (!color.equals(other.color)) {
            return false;
        }

        return true;
    }
}
