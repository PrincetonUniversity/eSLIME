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
import cells.Cell;
import control.arguments.Argument;
import control.identifiers.Coordinate;
import layers.LayerManager;
import layers.cell.CellUpdateManager;

/**
 * Places a copy or copies of the current cell at the target site(s).
 * This uses the "clone" method, rather than the "divide" method, meaning
 * that the state of the cell is exactly preserved.
 *
 * Created by dbborens on 5/2/14.
 */
public class CloneTo extends Action{

    private TargetRule targetRule;

    // Highlight channels for the targeting and targeted cells
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    public CloneTo(BehaviorCell callback, LayerManager layerManager, TargetRule targetRule, Argument<Integer> selfChannel, Argument<Integer> targetChannel) {
        super(callback, layerManager);
        this.targetRule = targetRule;
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
    }

    @Override
    public void run(Coordinate caller) {
        BehaviorCell callerCell = resolveCaller(caller);

        Coordinate self = getOwnLocation();

        Coordinate[] targets = targetRule.report(callerCell);

        CellUpdateManager u = getLayerManager().getCellLayer().getUpdateManager();

        for (Coordinate target : targets) {
            doSanityCheck(target);

            // Make clone
            Cell child = getCallback().clone();

            // Place clone at target site
            u.place(child, target);

            // Highlight sites
            highlight(target, self);
        }

    }

    private void doSanityCheck(Coordinate target) {
        // Sanity check: target should be vacant
        if (getLayerManager().getCellLayer().getViewer().isOccupied(target)) {
            throw new IllegalStateException("Illegal state: a cell is" +
                    " attempting to clone itself into an occupied site.");
        }
    }

    private void highlight(Coordinate target, Coordinate ownLocation) {
        doHighlight(targetChannel, target);
        doHighlight(selfChannel, ownLocation);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CloneTo cloneTo = (CloneTo) o;

        if (targetRule != null ? !targetRule.equals(cloneTo.targetRule) : cloneTo.targetRule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = targetRule != null ? targetRule.hashCode() : 0;
        result = 31 * result + (selfChannel != null ? selfChannel.hashCode() : 0);
        result = 31 * result + (targetChannel != null ? targetChannel.hashCode() : 0);
        return result;
    }

    @Override
    public Action clone(BehaviorCell child) {
        TargetRule clonedTargeter = targetRule.clone(child);
        return new CloneTo(child, getLayerManager(), clonedTargeter, selfChannel, targetChannel);
    }
}
