/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import agent.targets.TargetRule;
import cells.BehaviorCell;
import cells.Cell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellUpdateManager;
import processes.discrete.ShoveHelper;

import java.util.List;
import java.util.Random;

/**
 * Places a copy or copies of the current cell at the target site(s).
 * This uses the "replicate" method, rather than the "divide" method, meaning
 * that the state of the cell is exactly preserved.
 *
 * Created by dbborens on 5/2/14.
 */
public class ExpandTo extends Action{

    // Highlight channels for the targeting and targeted cells
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    // Displaces cells along a trajectory in the event that the cell is
    // divided into an occupied site and replace is disabled.
    private ShoveHelper shoveHelper;

    private Random random;

    private TargetRule targetRule;

    public ExpandTo(BehaviorCell callback, LayerManager layerManager, TargetRule targetRule,
                    Argument<Integer> selfChannel, Argument<Integer> targetChannel, Random random) {
        super(callback, layerManager);
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
        this.random = random;
        this.targetRule = targetRule;

        shoveHelper = new ShoveHelper(layerManager, random);
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        BehaviorCell callerCell = resolveCaller(caller);
        List<Coordinate> targets = targetRule.report(callerCell);
        for (Coordinate target : targets) {
            preferentialExpand(target);
        }
    }

    private void preferentialExpand(Coordinate target) throws HaltCondition {
        Coordinate origin = getOwnLocation();

        // Find out the shortest shoving path available, given the specified
        // parent and its preferred progeny destination.
        DisplacementOption shortestOption = getShortestOption(target);

        // Create a vacancy either at the parent or child site, depending on
        // which had a shorter shoving path.
        doShove(shortestOption);

        // Now that the cells have been shoved toward the vacancy, the formerly
        // occupied site is now vacant.
        Coordinate newlyVacant = shortestOption.occupied;

        // Place a cloned cell at the newly vacated position.
        cloneToVacancy(newlyVacant);

        // Clean up out-of-bounds cells.
        shoveHelper.removeImaginary();

        // Highlight the parent and target locations.
        highlight(target, origin);
    }

    private void cloneToVacancy(Coordinate vacancy) throws HaltCondition {
        CellUpdateManager u = getLayerManager().getCellLayer().getUpdateManager();

        // Clone parent.
        Cell child = getCallback().replicate();

        // Place child in parent location.
        u.place(child, vacancy);
    }

    private void doShove(DisplacementOption shortestOption) throws HaltCondition {
        Coordinate occupied = shortestOption.occupied;
        Coordinate vacant = shortestOption.vacant;
        shoveHelper.shove(occupied, vacant);
    }

    /**
     * Compare the distance between the origin and its nearest vacancy,
     * and the target and its nearest vacancy. Report the closer one as the
     * preferred direction of expansion.
     */
    private DisplacementOption getShortestOption(Coordinate target) throws HaltCondition {
        Coordinate origin = getOwnLocation();

        // Get option that starts with origin.
        DisplacementOption originOption = getOption(origin);

        // Get option that starts with target.
        DisplacementOption targetOption = getOption(target);

        // Origin closer to vacancy?
        if (originOption.distance < targetOption.distance) {
            return originOption;
        // Target closer to vacancy?
        } else if (originOption.distance > targetOption.distance) {
            return targetOption;
        // Same?
        } else {
            // The coin toss arbitrarily favors shoving parent on true.
            return (random.nextBoolean() ? originOption : targetOption);
        }
    }

    private DisplacementOption getOption(Coordinate start) throws HaltCondition {
        Geometry geom = getLayerManager().getCellLayer().getGeometry();
        Coordinate end = shoveHelper.chooseVacancy(start);
        int distance = geom.getL1Distance(start, end, Geometry.APPLY_BOUNDARIES);

        DisplacementOption ret = new DisplacementOption();
        ret.occupied = start;
        ret.vacant = end;
        ret.distance = distance;

        return ret;
    }

    private void highlight(Coordinate target, Coordinate ownLocation) throws HaltCondition {
        doHighlight(targetChannel, target);
        doHighlight(selfChannel, ownLocation);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        TargetRule clonedTargetRule = targetRule.clone(child);
        return new ExpandTo(child, getLayerManager(), clonedTargetRule, selfChannel,
                targetChannel, random);
    }

    private class DisplacementOption {
        // This is the site that would start out occupied and end up vacant
        // (unless occupied == vacant).
        public Coordinate occupied;

        // This is the site that would start out vacant and end up occupied
        // (unless occupied == vacant).
        public Coordinate vacant;

        // L1 distance between origin and vacancy.
        public int distance;
    }
}
