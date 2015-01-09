/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action;

import agent.action.*;
import agent.targets.TargetRule;
import cells.BehaviorCell;
import control.GeneralParameters;
import control.arguments.Argument;
import factory.agent.targets.TargetFactory;
import factory.control.arguments.IntegerArgumentFactory;
import layers.LayerManager;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

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
                return die(e, callback, layerManager, p);
            case "adjust-health":
                return adjustHealth(e, callback, layerManager);
            case "trigger":
                return trigger(e, callback, layerManager, p);
            case "clone":
                return cloneTo(e, callback, layerManager, p);
            case "expand":
                return expand(e, callback, layerManager, p);
            case "expand-to":
                return expandTo(e, callback, layerManager, p);
            case "stochastic-choice":
                return stochasticChoice(e, callback, layerManager, p);
            case "swap":
                return swap(e, callback, layerManager, p);
            case "null":
                return nullAction();
            default:
                String msg = "Unrecognized action '" + actionName + "'.";
                throw new IllegalArgumentException(msg);
        }
    }

    private static Action nullAction() {
        return new NullAction();
    }

    private static Action stochasticChoice(Element e, BehaviorCell callback, LayerManager layerManager,
                                           GeneralParameters p) {

        DynamicActionRangeMap chooser = DynamicActionRangeMapFactory.instantiate(e, callback, layerManager, p);
        Random random = p.getRandom();

        Action action = new StochasticChoice(callback, layerManager, chooser, random);
        return action;
    }

    private static Action mockAction() {
        return new MockAction();
    }

    private static Action die(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Argument<Integer> channel = IntegerArgumentFactory.instantiate(e, "highlight", -1, p.getRandom());
        return new Die(callback, layerManager, channel);
    }

    private static Action trigger(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Element descriptor = e.element("target");
        TargetRule targetRule = TargetFactory.instantiate(callback, layerManager, descriptor, p);
        String behaviorName = e.element("behavior").getTextTrim();
        Argument<Integer> selfChannel = IntegerArgumentFactory.instantiate(e, "actor-highlight", -1, p.getRandom());
        Argument<Integer> targetChannel = IntegerArgumentFactory.instantiate(e, "target-highlight", -1, p.getRandom());
        return new Trigger(callback, layerManager, behaviorName, targetRule, selfChannel, targetChannel);
    }

    private static Action cloneTo(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Element descriptor = e.element("target");
        TargetRule targetRule = TargetFactory.instantiate(callback, layerManager, descriptor, p);
        Argument<Integer> selfChannel = IntegerArgumentFactory.instantiate(e, "actor-highlight", -1, p.getRandom());
        Argument<Integer> targetChannel = IntegerArgumentFactory.instantiate(e, "target-highlight", -1, p.getRandom());
        boolean noReplace = XmlUtil.getBoolean(e, "no-replacement");
        CloneTo ret = new CloneTo(callback, layerManager, targetRule, noReplace, selfChannel, targetChannel, p.getRandom());
        return ret;
    }

    private static Action swap(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Element descriptor = e.element("target");
        TargetRule targetRule = TargetFactory.instantiate(callback, layerManager, descriptor, p);
        Argument<Integer> selfChannel = IntegerArgumentFactory.instantiate(e, "actor-highlight", -1, p.getRandom());
        Argument<Integer> targetChannel = IntegerArgumentFactory.instantiate(e, "target-highlight", -1, p.getRandom());
        Swap ret = new Swap(callback, layerManager, targetRule, selfChannel, targetChannel);
        return ret;
    }

    private static Action expandTo(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Element descriptor = e.element("target");
        TargetRule targetRule = TargetFactory.instantiate(callback, layerManager, descriptor, p);
        Argument<Integer> selfChannel = IntegerArgumentFactory.instantiate(e, "actor-highlight", -1, p.getRandom());
        Argument<Integer> targetChannel = IntegerArgumentFactory.instantiate(e, "target-highlight", -1, p.getRandom());
        ExpandTo ret = new ExpandTo(callback, layerManager, targetRule, selfChannel, targetChannel, p.getRandom());
        return ret;
    }

    private static Action expand(Element e, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        Argument<Integer> selfChannel = IntegerArgumentFactory.instantiate(e, "actor-highlight", -1, p.getRandom());
        Argument<Integer> targetChannel = IntegerArgumentFactory.instantiate(e, "target-highlight", -1, p.getRandom());
        Expand ret = new Expand(callback, layerManager, selfChannel, targetChannel, p.getRandom());
        return ret;
    }

    private static Action adjustHealth(Element e, BehaviorCell callback, LayerManager layerManager) {
        String deltaStr = e.element("delta").getTextTrim();
        double delta = Double.valueOf(deltaStr);
        return new AdjustHealth(callback, layerManager, delta);
    }

}
