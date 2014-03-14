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

package io.project;

import agent.Behavior;
import agent.action.Action;
import agent.action.CompoundAction;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public abstract class BehaviorFactory {

    public static Behavior instantiate(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Action[] actionSequence = getActionSequence(e, callback, layerManager, p);
        Behavior ret = new Behavior(callback, layerManager, actionSequence);
        return ret;
    }

    public static CompoundAction instantiateAsCompoundAction(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Action[] actionSequence = getActionSequence(e, callback, layerManager, p);
        CompoundAction ret = new CompoundAction(callback, layerManager, actionSequence);
        return ret;
    }

    private static Action[] getActionSequence(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        // The children of the behavior element are actions. They
        // are loaded in order of execution, and are collectively
        // called the ActionSequence.
        ArrayList<Action> actionSequenceList = new ArrayList<>();
        for (Object o : e.elements()) {
            Element actionElement = (Element) o;
            Action action = ActionFactory.instantiate(actionElement, callback, layerManager, p);
            actionSequenceList.add(action);
        }

        Action[] actionSequence = actionSequenceList.toArray(new Action[0]);
        return actionSequence;
    }
}
