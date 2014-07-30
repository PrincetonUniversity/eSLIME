/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.targets;

import cells.BehaviorCell;
import control.identifiers.Coordinate;

/**
 * Created by dbborens on 2/14/14.
 */
public class MockTargetRule extends TargetRule {
    private Coordinate[] targets;
    private BehaviorCell lastCaller;

    public MockTargetRule() {
        super(null, null, -1, null);
    }

    public void setTargets(Coordinate[] targets) {
        this.targets = targets;
    }

    public BehaviorCell getLastCaller() {
        return lastCaller;
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        lastCaller = caller;
        return targets;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new MockTargetRule();
    }
}
