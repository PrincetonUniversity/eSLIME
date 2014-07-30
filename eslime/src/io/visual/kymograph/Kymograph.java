/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.kymograph;

import control.identifiers.Coordinate;
import geometry.Geometry;
import io.visual.Visualization;
import io.visual.VisualizationProperties;
import io.visual.highlight.HighlightManager;
import io.visual.map.CoordinateRenderer;
import io.visual.map.PixelTranslator;
import io.visual.map.PixelTranslatorFactory;
import layers.SystemState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/1/14.
 */
public class Kymograph extends Visualization {
    // All state members associated with this visualization.
    protected PixelTranslator translator;
    protected BufferedImage img;
    protected Graphics2D g;

    public Kymograph(VisualizationProperties properties) {
        this.properties = properties;
    }

    @Override
    public BufferedImage render(SystemState systemState) {

        CoordinateRenderer renderer = new CoordinateRenderer(g, translator, properties);
        for (Coordinate c : properties.getCoordinates()) {
            renderer.render(c, systemState);
        }

        if (systemState.getFrame() == properties.getFrames()[properties.getFrames().length - 1]) {
            return img;
        } else {
            return null;
        }
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
        translator = new KymoPixelTranslator();
        translator.init(properties);

        Coordinate pDims = translator.getImageDims();
        HighlightManager highlightManager = properties.getHighlightManager();
        highlightManager.init(translator);
        img = new BufferedImage(pDims.x(), pDims.y(), BufferedImage.TYPE_INT_RGB);

        g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        properties.getHighlightManager().setGraphics(g);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Kymograph)) {
            return false;
        }

        Kymograph other = (Kymograph) obj;

        if (!properties.equals(other.properties)) {
            return false;
        }

        if (!translator.equals(other.translator)) {
            return false;
        }

        return true;
    }

}
