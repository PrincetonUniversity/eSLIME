/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package agent.action;

import agent.targets.TargetRule;
import cells.BehaviorCell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;

/**
 * Causes the callback cell to swap locations with a neighbor.
 *
 * Created by dbborens on 5/26/14.
 */
public class Swap extends Action {
    private TargetRule targetRule;
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    public Swap(BehaviorCell callback, LayerManager layerManager,
                TargetRule targetRule, Argument<Integer> selfChannel,
                Argument<Integer> targetChannel) {
        super(callback, layerManager);
        this.targetRule = targetRule;
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        BehaviorCell callerCell = resolveCaller(caller);
        Coordinate self = getOwnLocation();
        Coordinate[] targets = targetRule.report(callerCell);

        if (targets.length != 1) {
            throw new IllegalStateException("Swap action requires exactly one " +
                    "target per event.");
        }

        Coordinate target = targets[0];

        getLayerManager().getCellLayer().getUpdateManager().swap(self, target);

        doHighlight(selfChannel, self);
        doHighlight(targetChannel, target);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Swap)) return false;

        Swap swap = (Swap) o;

        if (!targetRule.equals(swap.targetRule)) return false;

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        TargetRule clonedTargetRule = targetRule.clone(child);
        return new Swap(child, getLayerManager(), clonedTargetRule, selfChannel, targetChannel);
    }
}
