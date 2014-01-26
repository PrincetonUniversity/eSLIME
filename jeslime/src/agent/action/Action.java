package agent.action;

import cells.Cell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Actions are the consituent members of Behaviors. They
 * are strung together as an ordered list, called an
 * "action sequence." You can think of actions as anonymous,
 * predefined Behaviors.
 *
 * Created by David B Borenstein on 1/22/14.
 */
public abstract class Action {

    private final Cell callback;
    private final LayerManager layerManager;

    public Action(Cell callback, LayerManager layerManager) {
        this.callback = callback;
        this.layerManager = layerManager;
    }

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    protected Cell getCallback() {
        return callback;
    }

    public abstract void run(Coordinate caller);

    /**
     * Actions should be considered equal if they perform
     * the same function, but should NOT be concerned with
     * the identity of the callback.
     *
     * @param obj
     * @return
     */
    public abstract boolean equals(Object obj);

    public abstract Action clone(Cell child);
}
