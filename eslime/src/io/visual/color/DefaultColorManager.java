/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
