package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

import java.util.Random;

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
    public TargetSelf(BehaviorCell callback, LayerManager layerManager, int maximum, Random random) {
        super(callback, layerManager, maximum, random);
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);
        Coordinate[] arr = new Coordinate[] { self };
        return arr;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetSelf(child, layerManager, maximum, random);
    }
}
