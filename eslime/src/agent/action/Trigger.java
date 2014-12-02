/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import agent.targets.TargetRule;
import cells.BehaviorCell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;

import java.util.List;

/**
 * Created by dbborens on 2/11/14.
 */
public class Trigger extends Action {

    private String behaviorName;
    private TargetRule targetRule;

    // Highlight channels for the targeting and targeted cells
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    /**
     * Trigger a predesignated behavior in a cell or set of cells designated by a
     * targeting rule.
     *
     * @param callback     The cell associated with this behavior
     * @param layerManager
     * @param behaviorName The name of the behavior to be triggered in the targets.
     * @param targetRule   The targeting rule used to identify targets when called.
     */
    public Trigger(BehaviorCell callback, LayerManager layerManager, String behaviorName, TargetRule targetRule, Argument<Integer> selfChannel, Argument<Integer> targetChannel) {
        super(callback, layerManager);
        this.behaviorName = behaviorName;
        this.targetRule = targetRule;
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        BehaviorCell callerCell = resolveCaller(caller);

        // Since the Trigger behavior is the cause of the triggered behaviors,
        // the caller for the triggered behaviors is this cell.
        Coordinate self = getOwnLocation();

        List<Coordinate> targets = targetRule.report(callerCell);

        for (Coordinate target : targets) {
            // We require an occupied cell for the target of trigger actions.
            BehaviorCell targetCell = getWithCast(target);
            if (targetCell == null) {
                continue;
            }
            targetCell.trigger(behaviorName, self);
            highlight(target, self);
        }
    }

    private void highlight(Coordinate target, Coordinate ownLocation) throws HaltCondition {
        doHighlight(targetChannel, target);
        doHighlight(selfChannel, ownLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Trigger)) {
            return false;
        }

        Trigger other = (Trigger) obj;

        if (!other.behaviorName.equalsIgnoreCase(this.behaviorName)) {
            return false;
        }

        if (!other.targetRule.equals(this.targetRule)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        TargetRule clonedTargeter = targetRule.clone(child);
        Trigger cloned = new Trigger(child, getLayerManager(), behaviorName, clonedTargeter, selfChannel, targetChannel);
        return cloned;
    }
}
