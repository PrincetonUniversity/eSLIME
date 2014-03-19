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

import agent.action.Action;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Attribute;
import org.dom4j.Element;
import structural.RangeMap;
import structural.GeneralParameters;

import java.util.List;

/**
 * Constructs an action chooser, which is used in construting a StochasticChoice
 * object. Each choice has a weighting and one or more actions to be executed in
 * sequence. If more than one action is specified, the actions are executed in
 * the order specified whenever the option is chosen.
 *
 * For examples, see
 * Created by dbborens on 3/6/14.
 */
public abstract class ActionChooserFactory {

    public static RangeMap<Action> instantiate(Element base, BehaviorCell callback, LayerManager layerManager,
                                              GeneralParameters p) {

        RangeMap<Action> chooser = new RangeMap<>();
        // Iterate through all enumerated options.
        for (Object o : base.elements("option")) {
            Element option = (Element) o;

            // We expect exactly one child element for a stochastic choice option

            Action action = getAction(option, callback, layerManager, p);
            double weight = getWeight(option);

            chooser.add(action, weight);
        }

        chooser.close();
        return (chooser);
    }

    private static Action getAction(Element option, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        List elements = option.elements();
        if (elements.size() == 0) {
            throw new IllegalArgumentException("Expected an action or list of actions for stochastic choice option.");
        } else if (elements.size() == 1) {
            Element child = (Element) option.elements().iterator().next();
            return ActionFactory.instantiate(child, callback, layerManager, p);
        } else {
            return BehaviorFactory.instantiateAsCompoundAction(option, callback, layerManager, p);
        }
    }

    private static double getWeight(Element option) {
        // Get the weighting for this option.
        Attribute weightAttrib = option.attribute("weight");
        if (weightAttrib == null) {
            throw new IllegalArgumentException("Missing argument 'weight' in <stochastic-choice> tag.");
        }

        String weightStr = weightAttrib.getText();

        double weight = Double.valueOf(weightStr);

        return weight;
    }
}
