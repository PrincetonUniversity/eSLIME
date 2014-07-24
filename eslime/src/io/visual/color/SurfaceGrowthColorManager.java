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

package io.visual.color;

import control.identifiers.Coordinate;
import geometry.Geometry;
import io.visual.HSLColor;
import layers.SystemState;

import java.awt.*;

/**
 * Color manager that shows surface cells in a vibrant color, and interior
 * cells in a dull color. This operates as an overlay on an existing color
 * scheme.
 *
 * Created by dbborens on 7/23/14.
 */
public class SurfaceGrowthColorManager extends ColorManager {

    private ColorManager base;
    private double lightness;
    private double saturation;

    public SurfaceGrowthColorManager(ColorManager base,
                                     double lightness, double saturation) {
        this.base = base;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    @Override
    public Color getColor(Coordinate c, SystemState systemState) {
        Color baseColor = base.getColor(c, systemState);
        HSLColor baseHSL = new HSLColor(baseColor);

        boolean isInterior = checkIsInterior(c, systemState);
        // Check whether point is interior or exterior
        // Scale lightness appropriately
        // Convert back to RGB
        return null;
    }

    private boolean checkIsInterior(Coordinate c, SystemState systemState) {
        Geometry geom = getGeometry();
        Coordinate[] neighbors = geom.getNeighbors(c, Geometry.EXCLUDE_BOUNDARIES);

        for (Coordinate neighbor : neighbors) {
            int state = systemState.getLayerManager().getCellLayer().getViewer().getState(neighbor);

            // If any neighbor is 0 (vacant), the point is not interior
            if (state == 0) {
                return false;
            }

        }

        // If none of the neighbors are vacant, the point is interior
        return true;
    }

    @Override
    public Color getBorderColor() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
