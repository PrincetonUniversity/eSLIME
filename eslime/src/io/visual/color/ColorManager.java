package io.visual.color;

import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.SystemState;

import java.awt.*;

/**
 * Created by dbborens on 4/1/14.
 */
public abstract class ColorManager {

    private Geometry geometry;

    public void init(Geometry geometry) {
        this.geometry = geometry;
    }

    public abstract Color getColor(Coordinate c, SystemState systemState);

    public abstract Color getBorderColor();

    @Override
    public abstract boolean equals(Object obj);

    protected Geometry getGeometry() {
        return geometry;
    }

}
