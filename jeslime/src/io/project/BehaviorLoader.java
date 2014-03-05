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

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;

import java.util.HashMap;

/**
 * The BehaviorLoader is used to load behaviors and their names into a
 * BehaviorDispatcher.
 *
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorLoader {

    // Contains simulation environment information
    private final LayerManager layerManager;

    // The behavior dispatcher into which mappings should be loaded
    private final BehaviorDispatcher behaviorDispatcher;

    // The cell whose behaviors are being loaded
    private final BehaviorCell callback;

    private final GeneralParameters p;

    public BehaviorLoader(BehaviorDispatcher behaviorDispatcher, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        this.behaviorDispatcher = behaviorDispatcher;
        this.callback = callback;
        this.layerManager = layerManager;
        this.p = p;
    }

    public void loadAllBehaviors(Element behaviorsRoot) {
        for (Object o : behaviorsRoot.elements()) {
            loadBehavior(o);
        }
    }

    protected void loadBehavior(Object o) {
        Element e = (Element) o;
        String name = e.getName();
        Behavior behavior = BehaviorFactory.instantiate(e, callback, layerManager, p);
        behaviorDispatcher.map(name, behavior);
    }

}
