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

package io.project;

import agent.action.*;
import agent.targets.TargetRule;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.Chooser;
import structural.GeneralParameters;

import java.util.Random;

/**
 * Created by David B Borenstein on 1/22/14.
 */
public class ActionFactory {

    public static Action instantiate(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        String actionName = e.getName();

        switch (actionName) {
            case "mock":
                return mockAction();
            case "die":
                return die(callback, layerManager);
            case "adjust-fitness":
                return adjustFitness(e, callback, layerManager);
            case "trigger":
                return trigger(e, callback, layerManager, p);
            case "stochastic-choice":
                return stochasticChoice(e, callback, layerManager, p);
            case "null":
                return null;
            default:
                String msg = "Unrecognized action '" + actionName + "'.";
                throw new IllegalArgumentException(msg);
        }
    }

    private static Action stochasticChoice(Element e, BehaviorCell callback, LayerManager layerManager,
                                           GeneralParameters p) {

        Chooser<Action> chooser = ActionChooserFactory.instantiate(e, callback, layerManager, p);
        Random random = p.getRandom();

        Action action = new StochasticChoice(callback, layerManager, chooser, random);
        return action;
    }

    private static Action mockAction() {
        return new MockAction();
    }

    private static Action die(BehaviorCell callback, LayerManager layerManager) {
        return new Die(callback, layerManager);
    }

    private static Action trigger(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Random random = p.getRandom();
        Element descriptor = e.element("target");
        TargetRule targetRule = TargetFactory.instantiate(callback, layerManager, descriptor, random);
        String behaviorName = e.element("behavior").getTextTrim();
        return new Trigger(callback, layerManager, behaviorName, targetRule);
    }

    private static Action adjustFitness(Element e, BehaviorCell callback, LayerManager layerManager) {
        String deltaStr = e.element("delta").getTextTrim();
        double delta = Double.valueOf(deltaStr);
        return new AdjustFitness(callback, layerManager, delta);
    }

}
