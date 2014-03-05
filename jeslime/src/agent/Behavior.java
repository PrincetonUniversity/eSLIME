/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package agent;

import agent.action.Action;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.identifiers.Coordinate;

/**
 * A Behavior is an ordered sequence of Actions, associated
 * with a particular agent cell and invoked by name. Behaviors
 * can be triggered (invoked) either by the actions of other
 * cells or directly via a top-down process in your model.
 *
 * Each cell has its own set of Behaviors, which can affect the
 * neighborhood of the cell as well as the cell itself. These
 * Behaviors can include Actions that trigger the Behaviors of
 * neighboring cells.
 *
 * The defining feature of a Behavior is its ordered list of
 * Actions, called the "action sequence." When triggered (invoked),
 * the Actions in the action sequence are fired one at a time.
 *
 * Created by David B Borenstein on 1/21/14.
 */
public class Behavior {

    private final Cell callback;
    private final LayerManager layerManager;

    // Each action in the actionSequence array is fired,
    // in order, when the trigger(...) method is invoked.
    private final Action[] actionSequence;

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    public Cell getCallback() {
        return callback;
    }

    public Behavior(Cell callback, LayerManager layerManager, Action[] actionSequence) {
        this.callback = callback;
        this.layerManager = layerManager;
        this.actionSequence = actionSequence;
    }

    public void run(Coordinate caller) {
        for (Action action : actionSequence) {
            action.run(caller);
        }
    }

    /**
     * Behaviors are equal if and only if their action sequences
     * consist of an equivalent list of actions.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        // The object should be a behavior.
        if (!(obj instanceof Behavior)) {
            return false;
        }

        Behavior other = (Behavior) obj;

        // The object should have the same number of actions.
        if (other.getActionSequence().length != this.getActionSequence().length) {
            return false;
        }

        // Each action should be equivalent, and in the correct order.
        for (int i = 0; i < getActionSequence().length; i++) {
            Action p = this.getActionSequence()[i];
            Action q = other.getActionSequence()[i];
            if (!actionsEqual(p, q)) {
                return false;
            }
        }

        // The behaviors are equivalent.
        return true;
    }

    private boolean actionsEqual(Action p, Action q) {
        if (p == null && q == null) {
            return true;
        }

        if (p == null && q != null) {
            return false;
        }

        if (q == null && p != null) {
            return false;
        }

        if (!p.equals(q)) {
            return false;
        }

        return true;
    }

    protected Action[] getActionSequence() {
        return actionSequence;
    }

    public Behavior clone(BehaviorCell child) {
        int n = actionSequence.length;
        Action[] clonedActionSequence = new Action[n];
        for (int i = 0; i < n; i++) {
            Action action = actionSequence[i];
            Action clonedAction = action.clone(child);
            clonedActionSequence[i] = clonedAction;
        }

        Behavior clone = new Behavior(child, layerManager, clonedActionSequence);
        return clone;
    }
}
