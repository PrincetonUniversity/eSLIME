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
import layers.SystemState;

import java.awt.*;

/**
 * Created by dbborens on 4/1/14.
 */
public class DefaultColorManager extends ColorManager {

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DefaultColorManager);
    }

    @Override
    public Color getColor(Coordinate c, SystemState systemState) {
        int state = systemState.getLayerManager().getCellLayer().getViewer().getState(c);

        switch (state) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            case 3:
                return Color.GREEN;
            default:
                throw new UnsupportedOperationException("Default color manager supports only states 1 and 2, or dead.");
        }
    }

    @Override
    public Color getBorderColor() {
        return Color.DARK_GRAY;
    }
}
