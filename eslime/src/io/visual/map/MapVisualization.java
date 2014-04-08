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

import control.identifiers.Coordinate;
import geometry.Geometry;
import io.visual.Visualization;
import io.visual.highlight.HighlightManager;
import layers.SystemState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/1/14.
 */
public class MapVisualization implements Visualization {
    // All state members associated with this visualization.
    protected MapState members;
    protected PixelTranslator translator;

    public MapVisualization(MapState members) {
        this.members = members;
    }

    @Override
    public BufferedImage render(SystemState systemState) {
        Coordinate pDims = translator.getImageDims();
        BufferedImage img = new BufferedImage(pDims.x(), pDims.y(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        members.getHighlightManager().setGraphics(g);

        CoordinateRenderer renderer = new CoordinateRenderer(g, translator, members);
        for (Coordinate c : members.getCoordinates()) {
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
        // At the moment, only highlight channel 0 is supported.
        return new int[]{0};
    }

    @Override
    public void init(Geometry geometry) {
        Coordinate[] coordinates = geometry.getCanonicalSites();
        members.setCoordinates(coordinates);
        translator = PixelTranslatorFactory.instantiate(geometry);
        translator.init(members);
        HighlightManager highlightManager = members.getHighlightManager();
        highlightManager.init(translator);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapVisualization)) {
            return false;
        }

        MapVisualization other = (MapVisualization) obj;

        if (!members.equals(other.members)) {
            return false;
        }

        if (!translator.equals(other.translator)) {
            return false;
        }

        return true;
    }
}
