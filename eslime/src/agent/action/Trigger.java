/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package agent.action;

import agent.targets.TargetRule;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import layers.cell.CellLookupManager;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 2/11/14.
 */
public class Trigger extends Action {

    private String behaviorName;
    private TargetRule targetRule;

    /**
     * Trigger a predesignated behavior in a cell or set of cells designated by a
     * targeting rule.
     *
     * @param callback     The cell associated with this behavior
     * @param layerManager
     * @param behaviorName The name of the behavior to be triggered in the targets.
     * @param targetRule   The targeting rule used to identify targets when called.
     */
    public Trigger(BehaviorCell callback, LayerManager layerManager, String behaviorName, TargetRule targetRule) {
        super(callback, layerManager);
        this.behaviorName = behaviorName;
        this.targetRule = targetRule;
    }

    @Override
    public void run(Coordinate caller) {
        BehaviorCell callerCell = resolveCaller(caller);

        // Since the Trigger behavior is the cause of the triggered behaviors, the caller for the
        // triggered behaviors is this cell.
        Coordinate self = getOwnLocation();

        Coordinate[] targets = targetRule.report(callerCell);

        for (Coordinate target : targets) {
            // We require an occupied cell for the target of trigger actions.
            BehaviorCell targetCell = getWithCast(target);
            targetCell.trigger(behaviorName, self);
        }
    }

    /**
     * Returns the location of the cell whose behavior this is.
     *
     * @return
     */
    private Coordinate getOwnLocation() {
        CellLookupManager lookup = getLayerManager().getCellLayer().getLookupManager();
        BehaviorCell self = getCallback();
        Coordinate location = lookup.getCellLocation(self);
        return location;
    }

    private BehaviorCell resolveCaller(Coordinate caller) {
        // The caller is null, indicating that the call came from
        // a top-down process. Return null.
        if (caller == null) {
            return null;
        }

        // Blow up unless target coordinate contains a behavior cell.
        // In that case, return that cell.
        BehaviorCell callerCell = getWithCast(caller);

        return callerCell;
    }

    private BehaviorCell getWithCast(Coordinate coord) {
        CellLayerViewer viewer = getLayerManager().getCellLayer().getViewer();

        if (!viewer.isOccupied(coord)) {
            throw new IllegalStateException("Expected, but did not find, an occupied site at " + coord
                    + ".");
        }

        Cell putative = viewer.getCell(coord);

        if (!(putative instanceof BehaviorCell)) {
            throw new UnsupportedOperationException("Only BehaviorCells and top-down processes may trigger behaviors.");
        }

        BehaviorCell result = (BehaviorCell) putative;

        return result;
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
        Trigger cloned = new Trigger(child, getLayerManager(), behaviorName, clonedTargeter);
        return cloned;
    }
}
