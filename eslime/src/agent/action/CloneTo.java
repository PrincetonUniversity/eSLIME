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
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import layers.cell.CellUpdateManager;

import java.util.Random;

/**
 * Places a copy or copies of the current cell at the target site(s).
 * This uses the "clone" method, meaning that the state of the cell is
 * exactly preserved.
 *
 * Created by dbborens on 5/2/14.
 */
public class CloneTo extends Action{

    private TargetRule targetRule;

    // Replace occupied sites?
    private boolean noReplace;

    // Highlight channels for the targeting and targeted cells
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    private Random random;

    public CloneTo(BehaviorCell callback, LayerManager layerManager,
                   TargetRule targetRule, boolean noReplace,
                   Argument<Integer> selfChannel,
                   Argument<Integer> targetChannel, Random random) {

        super(callback, layerManager);
        this.targetRule = targetRule;
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
        this.noReplace = noReplace;
        this.random = random;
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        BehaviorCell callerCell = resolveCaller(caller);

        Coordinate self = getOwnLocation();

        Coordinate[] targets = targetRule.report(callerCell);

        CellUpdateManager u = getLayerManager().getCellLayer().getUpdateManager();
        CellLayerViewer v = getLayerManager().getCellLayer().getViewer();

        for (Coordinate target : targets) {

            // Make clone
            Cell child = getCallback().clone();

            // Place clone at target site
            if (!v.isOccupied(target)) {
                u.place(child, target);
            } else if (noReplace) {
                throw new IllegalStateException("In CloneTo: Attempted to " +
                        "place child at occupied position (leading to " +
                        "replacement), but <no-replacment /> flag is set.");
            } else {
                u.banish(target);
                u.place(child, target);
            }
            // Highlight sites
            highlight(target, self);
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
        return new CloneTo(child, getLayerManager(), clonedTargeter, noReplace,
                selfChannel, targetChannel, random);
    }
}
