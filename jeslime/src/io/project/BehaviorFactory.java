package io.project;

import agent.Behavior;
import agent.action.Action;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public abstract class BehaviorFactory {

    public static Behavior instantiate(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
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
        Behavior ret = new Behavior(callback, layerManager, actionSequence);
        return ret;
    }
}
