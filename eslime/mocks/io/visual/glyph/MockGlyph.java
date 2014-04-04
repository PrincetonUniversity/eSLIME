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

import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/2/14.
 */
public class MockGlyph extends Glyph {
    private BufferedImage image;
    private Coordinate lastOverlaid;

    public MockGlyph() {
        lastOverlaid = null;
    }

    @Override
    protected void internalInit() {
    }

    public Coordinate getLastOverlaid() {
        return lastOverlaid;
    }

    @Override
    public void overlay(Coordinate c) {
        this.lastOverlaid = c;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockGlyph);
    }
}
