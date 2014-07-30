/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.map;

import control.identifiers.Coordinate;
import io.visual.VisualizationProperties;
import io.visual.highlight.HighlightManager;
import layers.SystemState;

import java.awt.*;

/**
 * Created by dbborens on 4/1/14.
 */
public class CoordinateRenderer {

    private Graphics2D graphics;
    private PixelTranslator translator;
    private VisualizationProperties mapState;

    public CoordinateRenderer(Graphics2D graphics, PixelTranslator translator, VisualizationProperties mapState) {
        this.graphics = graphics;
        this.translator = translator;
        this.mapState = mapState;
    }

    public void render(Coordinate c, SystemState systemState) {
        renderFill(c, systemState);

        if (mapState.getOutline() != 0) {
            renderOutline(c, systemState);
        }

        renderHighlights(c, systemState);
    }

    private void renderFill(Coordinate toRender, SystemState systemState) {
        Color color = mapState.getColorManager().getColor(toRender, systemState);
        Polygon p = translator.makePolygon(toRender, systemState.getFrame(), systemState.getTime());

        // Flood region with background color
        graphics.setColor(color);
        graphics.fillPolygon(p);
    }

    private void renderOutline(Coordinate toRender, SystemState systemState) {
        Color color = mapState.getColorManager().getBorderColor();
        Polygon p = translator.makePolygon(toRender, systemState.getFrame(), systemState.getTime());

        // Flood region with background color
        graphics.setColor(color);
        graphics.drawPolygon(p);
    }

    private void renderHighlights(Coordinate toRender, SystemState systemState) {
        HighlightManager highlightManager = mapState.getHighlightManager();
        highlightManager.render(toRender, systemState);
    }

}
