package io.project;

import agent.action.*;
import agent.targets.TargetRule;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
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
            case "null":
                return null;
            default:
                String msg = "Unrecognized action '" + actionName + "'.";
                throw new IllegalArgumentException(msg);
        }
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
