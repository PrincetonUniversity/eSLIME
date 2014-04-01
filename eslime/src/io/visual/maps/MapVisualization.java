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

package io.visual.maps;

import geometry.Geometry;
import io.visual.Visualization;
import layers.SystemState;
import structural.identifiers.Coordinate;

import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/1/14.
 */
public class MapVisualization implements Visualization {
    // All state members associated with this visualization.
    protected MapState members;
    protected PixelTranslator translator;

    public MapVisualization(MapState members, PixelTranslator translator) {
        this.members = members;
        this.translator = translator;
    }

    @Override
    public BufferedImage render(SystemState systemState) {
        Coordinate pDims = translator.getImageDims();
        BufferedImage img = new BufferedImage(pDims.x(), pDims.y(), BufferedImage.TYPE_BYTE_INDEXED);
        CoordinateRenderer renderer = new CoordinateRenderer(img, translator, members);
        for (Coordinate c : members.getCoordinates()) {
            renderer.render(c, systemState);
        }

        return img;
    }

    @Override
    public void conclude() {
    }

    @Override
    public void init(Geometry geometry) {
        Coordinate[] coordinates = geometry.getCanonicalSites();
        members.setCoordinates(coordinates);
        translator.init(members);
    }
}
