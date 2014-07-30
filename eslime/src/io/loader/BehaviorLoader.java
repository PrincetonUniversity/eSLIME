/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.loader;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import control.GeneralParameters;
import factory.agent.BehaviorFactory;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * The BehaviorLoader is used to load behaviors and their names into a
 * BehaviorDispatcher.
 * <p/>
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
        // No behaviors specified? That's fine -- it's now just a base cell.
        if (behaviorsRoot == null) {
            return;
        }

        for (Object o : behaviorsRoot.elements()) {
            loadBehavior(o);
        }
    }

    public void loadBehavior(Object o) {
        Element e = (Element) o;
        String name = e.getName();
        Behavior behavior = BehaviorFactory.instantiate(e, callback, layerManager, p);
        behaviorDispatcher.map(name, behavior);
    }

}
