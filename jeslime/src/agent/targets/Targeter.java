package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 *
 * NOTE: Do not confuse 'callback' and 'caller.' The callback
 * is the cell associated with this Targeter object (i.e., whose
 * behavior is being described.) The caller is the cell that
 * triggered the behavior, if any.
 *
 * Created by dbborens on 2/7/14.
 */
public abstract class Targeter {

    protected BehaviorCell callback;
    protected LayerManager layerManager;

    /**
     *
     * @param callback The cell whose behavior is being described
     * @param layerManager
     */
    public Targeter(BehaviorCell callback, LayerManager layerManager){
        this.callback = callback;
        this.layerManager = layerManager;
    }

    /**
     * Returns the list of target coordinates that satisfy
     * the action's target descriptor.
     *
     * @param caller The cell that triggered the action.
     */
    public abstract Coordinate[] report(BehaviorCell caller);
}
