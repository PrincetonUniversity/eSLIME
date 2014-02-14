package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 2/14/14.
 */
public class MockTargetRule extends TargetRule {
    private Coordinate[] targets;
    private BehaviorCell lastCaller;

    public MockTargetRule() {
        super(null, null);
    }

    public void setTargets(Coordinate[] targets) {
        this.targets = targets;
    }

    public BehaviorCell getLastCaller() {
        return lastCaller;
    }

    @Override
    public Coordinate[] report(BehaviorCell caller) {
        lastCaller = caller;
        return targets;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new MockTargetRule();
    }
}
