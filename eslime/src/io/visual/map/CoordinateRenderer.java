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

import io.visual.highlight.HighlightManager;
import layers.SystemState;
import structural.identifiers.Coordinate;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/1/14.
 */
public class CoordinateRenderer {

    private BufferedImage image;
    private PixelTranslator translator;
    private MapState mapState;

    public CoordinateRenderer(BufferedImage image, PixelTranslator translator, MapState mapState) {
        this.image = image;
        this.translator = translator;
        this.mapState = mapState;
    }

    public void render(Coordinate c, SystemState systemState) {
        renderFill(c, systemState);
        renderOutline(c, systemState);
        renderHighlights(c, systemState);
    }

    private void renderFill(Coordinate toRender, SystemState systemState) {
        Graphics g = image.getGraphics();
        Color color = mapState.getColorManager().getColor(toRender, systemState);
        Polygon p = translator.makePolygon(toRender);

        // Flood region with background color
        g.setColor(color);
        g.fillPolygon(p);
    }

    private void renderOutline(Coordinate toRender, SystemState systemState) {
        Graphics g = image.getGraphics();
        Color color = mapState.getColorManager().getBorderColor();
        Polygon p = translator.makePolygon(toRender);

        // Flood region with background color
        g.setColor(color);
        g.drawPolygon(p);
    }

    private void renderHighlights(Coordinate toRender, SystemState systemState) {
        HighlightManager highlightManager = mapState.getHighlightManager();
        highlightManager.render(toRender, systemState);
    }

}
