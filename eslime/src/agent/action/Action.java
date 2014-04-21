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
import control.arguments.Argument;
import control.identifiers.Coordinate;
import layers.LayerManager;
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

    public abstract void run(Coordinate caller);

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

        Integer channel = channelArg.next();
        StepState stepState = getLayerManager().getStepState();
        stepState.highlight(toHighlight, channel);
    }
}
