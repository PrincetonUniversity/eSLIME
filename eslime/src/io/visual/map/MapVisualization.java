/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.map;

import control.identifiers.Coordinate;
import geometry.Geometry;
import io.visual.Visualization;
import io.visual.VisualizationProperties;
import io.visual.highlight.HighlightManager;
import layers.SystemState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/1/14.
 */
public class MapVisualization extends Visualization {
    // All state members associated with this visualization.
    protected PixelTranslator translator;

    public MapVisualization(VisualizationProperties properties) {
        this.properties = properties;
    }

    @Override
    public BufferedImage render(SystemState systemState) {
        System.out.println("Rendering frame " + systemState.getFrame());
        Coordinate pDims = translator.getImageDims();
        BufferedImage img = new BufferedImage(pDims.x(), pDims.y(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        properties.getHighlightManager().setGraphics(g);

        CoordinateRenderer renderer = new CoordinateRenderer(g, translator, properties);
        for (Coordinate c : properties.getCoordinates()) {
            renderer.render(c, systemState);
        }

        return img;
    }

    @Override
    public void conclude() {
    }

    @Override
    public String[] getSoluteIds() {
        // At the moment, no solute fields are supported.
        return new String[]{};
    }

    @Override
    public int[] getHighlightChannels() {
        return properties.getHighlightManager().getHighlightChannels();
    }

    @Override
    public void init(Geometry geometry, double[] times, int[] frames) {
        Coordinate[] coordinates = geometry.getCanonicalSites();
        properties.setCoordinates(coordinates);
        properties.setFrames(frames);
        properties.setTimes(times);
        translator = PixelTranslatorFactory.instantiate(geometry);
        translator.init(properties);
        HighlightManager highlightManager = properties.getHighlightManager();
        highlightManager.init(translator);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapVisualization)) {
            return false;
        }

        MapVisualization other = (MapVisualization) obj;

        if (!properties.equals(other.properties)) {
            return false;
        }

        if (!translator.equals(other.translator)) {
            return false;
        }

        return true;
    }

}
