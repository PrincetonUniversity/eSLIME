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

    protected final Cell callback;
    protected final LayerManager layerManager;

    public Action(Cell callback, LayerManager layerManager) {
        this.callback = callback;
        this.layerManager = layerManager;
    }

    public abstract void run(Coordinate caller);
}
