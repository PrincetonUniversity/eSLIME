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

import cells.BehaviorCell;
import cells.Cell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import layers.cell.CellLookupManager;
import processes.StepState;

/**
 * Actions are the consituent members of Behaviors. They
 * are strung together as an ordered list, called an
 * "action sequence." You can think of actions as anonymous,
 * predefined Behaviors.
 * <p/>
 * Created by David B Borenstein on 1/22/14.
 */
public abstract class Action {

    private final BehaviorCell callback;
    private final LayerManager layerManager;

    public Action(BehaviorCell callback, LayerManager layerManager) {
        this.callback = callback;
        this.layerManager = layerManager;
    }

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    protected BehaviorCell getCallback() {
        return callback;
    }

    public abstract void run(Coordinate caller) throws HaltCondition;

    /**
     * Returns the location of the cell whose behavior this is.
     *
     * @return
     */
    protected Coordinate getOwnLocation() {
        CellLookupManager lookup = getLayerManager().getCellLayer().getLookupManager();
        BehaviorCell self = getCallback();
        Coordinate location = lookup.getCellLocation(self);
        return location;
    }

    protected BehaviorCell resolveCaller(Coordinate caller) {
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

    protected BehaviorCell getWithCast(Coordinate coord) {
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

    /**
     * Actions should be considered equal if they perform
     * the same function, but should NOT be concerned with
     * the identity of the callback.
     *
     * @param obj
     * @return
     */
    public abstract boolean equals(Object obj);

    public abstract Action clone(BehaviorCell child);

    protected void doHighlight(Argument<Integer> channelArg, Coordinate toHighlight) {
        // If not using highlights, do nothing
        if (channelArg == null) {
            return;
        }

        if (!layerManager.getCellLayer().getGeometry().contains(toHighlight)) {
            return;
        }
        Integer channel = channelArg.next();
        StepState stepState = getLayerManager().getStepState();
        stepState.highlight(toHighlight, channel);
    }
}
