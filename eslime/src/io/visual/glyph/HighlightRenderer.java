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

import layers.SystemState;
import structural.identifiers.Coordinate;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbborens on 4/2/14.
 */
public class HighlightRenderer {
    private Map<Integer, Glyph> glyphMap;

    public HighlightRenderer() {
        glyphMap = new HashMap<>();
    }

    public void setGlyph(int channel, Glyph glyph) {
        glyphMap.put(channel, glyph);
    }

    public void setImage(BufferedImage image) {
        for (Glyph glyph : glyphMap.values()) {
            glyph.setImage(image);
        }
    }

    public void overlayGlyphs(Coordinate c, SystemState systemState) {
        for (int channel : glyphMap.keySet()) {
            if (systemState.isHighlighted(channel, c)) {
                Glyph glyph = glyphMap.get(channel);
                glyph.overlay(c);
            }
        }
    }
}
