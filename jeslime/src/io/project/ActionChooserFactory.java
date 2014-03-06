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

import agent.action.Action;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Attribute;
import org.dom4j.Element;
import structural.Chooser;
import structural.GeneralParameters;

import java.util.List;

/**
 * Created by dbborens on 3/6/14.
 */
public abstract class ActionChooserFactory {

    public static Chooser<Action> instantiate(Element base, BehaviorCell callback, LayerManager layerManager,
                                              GeneralParameters p) {

        Chooser<Action> chooser = new Chooser<>();
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
        Element actionElement = getOnlyChild(option);
        Action action = ActionFactory.instantiate(actionElement, callback, layerManager, p);
        return action;
    }

    private static Element getOnlyChild(Element option) {
        List elements = option.elements();

        // Verify that there is only one child element.
        if (elements.size() != 1) {
            throw new IllegalArgumentException("Expect exactly one child element for a stochastic choice option.");
        }

        Object child = elements.iterator().next();

        Element ret = (Element) child;

        return ret;
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
