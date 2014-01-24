package io.project;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;

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
    private final Cell callback;

    public BehaviorLoader(BehaviorDispatcher behaviorDispatcher, Cell callback, LayerManager layerManager) {
        this.behaviorDispatcher = behaviorDispatcher;
        this.callback = callback;
        this.layerManager = layerManager;
    }

    public void loadAllBehaviors(Element behaviorsRoot) {
        for (Object o : behaviorsRoot.elements()) {
            loadBehavior(o);
        }
    }

    protected void loadBehavior(Object o) {
        Element e = (Element) o;
        String name = e.getName();
        Behavior behavior = BehaviorFactory.instantiate(e, callback, layerManager);
        behaviorDispatcher.map(name, behavior);
    }

}
