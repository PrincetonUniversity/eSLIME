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

import io.visual.color.ColorManager;
import structural.identifiers.Coordinate;

/**
 * Container for all state members associated with a map visualization.
 * <p/>
 * Created by dbborens on 4/1/14.
 */
public class MapState {

    // Set of all coordinates to be considered.
    private Coordinate[] coordinates;

    // Helper object for setting colors.
    private ColorManager colorManager;


    // How long each edge should be. Sets visual scale.
    private double edge;

    public MapState(ColorManager colorManager, double edge) {
        this.colorManager = colorManager;
        this.edge = edge;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public double getEdge() {
        return edge;
    }
}
