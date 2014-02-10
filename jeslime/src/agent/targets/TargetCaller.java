package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetCaller extends Targeter {
    public TargetCaller(BehaviorCell callback, LayerManager layerManager) {
        super(callback, layerManager);
    }

    @Override
    public Coordinate[] report(BehaviorCell caller) {
        Coordinate coord = layerManager.getCellLayer().getLookupManager().getCellLocation(caller);
        Coordinate[] arr = new Coordinate[] { coord };
        return arr;
    }
}
