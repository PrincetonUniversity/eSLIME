package io.project;

import agent.action.Action;
import agent.action.MockAction;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by David B Borenstein on 1/22/14.
 */
public class ActionFactory {

    public static Action instantiate(Element e, Cell callback, LayerManager layerManager) {
        String actionName = e.getName();

        if (actionName.equalsIgnoreCase("mock")) {
            return new MockAction();
        } else if (actionName.equalsIgnoreCase("null")) {
            return null;
        } else {
            String msg = "Unrecognized action '" + actionName + "'.";
            throw new IllegalArgumentException(msg);
        }
    }
}
