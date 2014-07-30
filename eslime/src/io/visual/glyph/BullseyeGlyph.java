/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import control.identifiers.Coordinate;

import java.awt.*;

/**
 * Consists of three concentric, filled circles. The first and third have the same
 * color, and the inner one has a different color unless otherwise specified.
 * Created by dbborens on 4/4/14.
 */
public class BullseyeGlyph extends Glyph {
    private int[] radii;
    private Color[] colors;
    private double size;

    /**
     * @param primary   The color of the outer and center circle.
     * @param secondary The color of the middle circle.
     * @param size      The relative size of the dot, specified as a multiple of the edge size.
     */
    public BullseyeGlyph(Color primary, Color secondary, double size) {
        colors = new Color[]{primary, secondary, primary};
        this.size = size;
    }

    @Override
    protected void internalInit() {
        double radius = calcProportionalSize(size);

        int outer = (int) Math.round(radius);
        int middle = (int) Math.round(radius * .67);
        int inner = (int) Math.round(radius * .33);

        radii = new int[]{outer, middle, inner};
    }


    @Override
    public void overlay(Coordinate target, int frame, double time) {
        // First, get the center of the cell (in pixels).
        Coordinate center = translator.resolve(target, frame, time);

        for (int i = 0; i < 3; i++) {
            int radius = radii[i];
            Color color = colors[i];
            int x = center.x() - radius;
            int y = center.y() - radius;

            // Outermost circle.

            graphics.setColor(color);
            graphics.fillOval(x, y, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BullseyeGlyph)) {
            return false;
        }

        BullseyeGlyph other = (BullseyeGlyph) obj;
        if (size != other.size) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!colors[i].equals(other.colors[i])) {
                return false;
            }
        }

        return true;
    }
}
