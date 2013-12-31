package layers;

import geometry.Geometry;

/**
 * Created by dbborens on 12/11/13.
 */
public abstract class Layer {

    protected Geometry geometry;

    public abstract String getId();

    public Geometry getGeometry() {
        return geometry;
    }
}
