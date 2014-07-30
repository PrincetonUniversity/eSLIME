/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import control.identifiers.Coordinate;

import java.awt.*;

/**
 * Created by dbborens on 4/2/14.
 */
public class MockGlyph extends Glyph {
    private Graphics2D graphics;
    private Coordinate lastOverlaid;

    public MockGlyph() {
        lastOverlaid = null;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    protected void internalInit() {
    }

    public Coordinate getLastOverlaid() {
        return lastOverlaid;
    }

    @Override
    public void overlay(Coordinate c, int frame, double time) {
        this.lastOverlaid = c;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockGlyph);
    }
}
