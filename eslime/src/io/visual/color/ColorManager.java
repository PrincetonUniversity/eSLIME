package io.visual.color;

import layers.SystemState;
import structural.identifiers.Coordinate;

import java.awt.*;

/**
 * Created by dbborens on 4/1/14.
 */
public abstract class ColorManager {
    public abstract Color getColor(Coordinate c, SystemState systemState);

    public abstract Color getBorderColor();

    @Override
    public abstract boolean equals(Object obj);
}
