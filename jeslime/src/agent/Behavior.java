package agent;

import agent.action.Action;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.identifiers.Coordinate;

/**
 * A Behavior is an ordered sequence of Actions, associated
 * with a particular agent cell and invoked by name. Behaviors
 * can be triggered (invoked) either by the actions of other
 * cells or directly via a top-down process in your model.
 *
 * Each cell has its own set of Behaviors, which can affect the
 * neighborhood of the cell as well as the cell itself. These
 * Behaviors can include Actions that trigger the Behaviors of
 * neighboring cells.
 *
 * The defining feature of a Behavior is its ordered list of
 * Actions, called the "action sequence." When triggered (invoked),
 * the Actions in the action sequence are fired one at a time.
 *
 * Created by David B Borenstein on 1/21/14.
 */
public class Behavior {

    private final Cell callback;
    private final LayerManager layerManager;

    // Each action in the actionSequence array is fired,
    // in order, when the trigger(...) method is invoked.
    private final Action[] actionSequence;

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    protected Cell getCallback() {
        return callback;
    }

    public Behavior(Cell callback, LayerManager layerManager, Action[] actionSequence) {
        this.callback = callback;
        this.layerManager = layerManager;
        this.actionSequence = actionSequence;
    }

    public void trigger(Coordinate caller) {
        for (Action action : actionSequence) {
            action.run(caller);
        }
    }

    protected Action[] getActionSequence() {
        return actionSequence;
    }
}
