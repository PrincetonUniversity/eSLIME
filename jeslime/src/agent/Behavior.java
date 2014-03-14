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

package agent;

import agent.action.Action;
import agent.action.CompoundAction;
import cells.BehaviorCell;
import layers.LayerManager;

/**
 * A Behavior is an ordered sequence of Actions, associated
 * with a particular agent cell and invoked by name. Behaviors
 * can be triggered (invoked) either by the actions of other
 * cells or directly via a top-down process in your model.
 * <p/>
 * Each cell has its own set of Behaviors, which can affect the
 * neighborhood of the cell as well as the cell itself. These
 * Behaviors can include Actions that trigger the Behaviors of
 * neighboring cells.
 * <p/>
 * The defining feature of a Behavior is its ordered list of
 * Actions, called the "action sequence." When triggered (invoked),
 * the Actions in the action sequence are fired one at a time.
 * <p/>
 * Notably, the Behavior actually extends from a class of Action.
 * This is because it can be thought of an Action that contains
 * other actions.
 * <p/>
 * Created by David B Borenstein on 1/21/14.
 */
public class Behavior extends CompoundAction {

    @Override
    public boolean equals(Object obj) {
        // Behaviors are CompoundActions, but not vice versa
        if (!(obj instanceof Behavior)) {
            return false;
        }

        // Otherwise, the definition of equality for Behaviors
        // and CompoundActions are the same.
        return super.equals(obj);
    }

    public Behavior(BehaviorCell callback, LayerManager layerManager, Action[] actionSequence) {
        super(callback, layerManager, actionSequence);
    }

    @Override
    public Behavior clone(BehaviorCell child) {
        Action[] clonedActionSequence = cloneActionSequence(child);
        Behavior clone = new Behavior(child, layerManager, clonedActionSequence);
        return clone;
    }

}
