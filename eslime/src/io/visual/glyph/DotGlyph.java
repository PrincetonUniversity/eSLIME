/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import control.identifiers.Coordinate;

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
        radius = calcProportionalSize(size);
    }

    @Override
    public void overlay(Coordinate target, int frame, double time) {
        // First, get the center of the cell (in pixels).
        Coordinate center = translator.resolve(target, frame, time);

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
