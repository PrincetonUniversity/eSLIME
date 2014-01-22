package agent.behaviors;

import cells.Cell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public abstract class Behavior {

    private final Cell callback;
    private final LayerManager layerManager;

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    protected Cell getCallback() {
        return callback;
    }


    public Behavior(Cell callback, LayerManager layerManager) {
        this.callback = callback;
        this.layerManager = layerManager;
    }


    public abstract void run(Coordinate caller);
}
