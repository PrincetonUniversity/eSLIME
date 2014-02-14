package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 *
 * The TargetSelf object always returns the cell that is performing
 * the action.
 *
 * Created by dbborens on 2/7/14.
 */
public class TargetSelf extends TargetRule {

    public TargetSelf(BehaviorCell callback, LayerManager layerManager) {
        super(callback, layerManager);
    }

    @Override
    public Coordinate[] report(BehaviorCell caller) {
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);
        Coordinate[] arr = new Coordinate[] { self };
        return arr;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetSelf(child, layerManager);
    }
}
