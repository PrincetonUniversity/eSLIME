/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.highlight;

import control.identifiers.Coordinate;
import io.visual.glyph.Glyph;
import io.visual.map.PixelTranslator;
import layers.SystemState;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbborens on 4/2/14.
 */
public class HighlightManager {
    private Map<Integer, Glyph> glyphMap;

    public HighlightManager() {
        glyphMap = new HashMap<>();
    }

    public void setGlyph(int channel, Glyph glyph) {
        glyphMap.put(channel, glyph);
    }

    public void setGraphics(Graphics2D graphics) {
        for (Glyph glyph : glyphMap.values()) {
            glyph.setGraphics(graphics);
        }
    }

    public void render(Coordinate c, SystemState systemState) {
        for (int channel : glyphMap.keySet()) {
            if (systemState.isHighlighted(channel, c)) {
                Glyph glyph = glyphMap.get(channel);
                glyph.overlay(c, systemState.getFrame(), systemState.getTime());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HighlightManager)) {
            return false;
        }

        HighlightManager other = (HighlightManager) obj;

        if (other.glyphMap.size() != glyphMap.size()) {
            return false;
        }

        for (Integer key : glyphMap.keySet()) {
            if (!other.glyphMap.containsKey(key)) {
                return false;
            }

            Glyph p = glyphMap.get(key);
            Glyph q = other.glyphMap.get(key);
            if (!p.equals(q)) {
                return false;
            }
        }

        return true;
    }

    public void init(PixelTranslator translator) {
        for (Glyph glyph : glyphMap.values()) {
            glyph.init(translator);
        }
    }

    public int[] getHighlightChannels() {
        int[] channels = new int[glyphMap.size()];
        int i = 0;
        for (Integer channel : glyphMap.keySet()) {
            channels[i] = channel;
            i++;
        }

        return channels;
    }
}
