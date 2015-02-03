/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent;

import agent.Behavior;
import agent.action.Action;
import cells.BehaviorCell;
import control.GeneralParameters;
import control.arguments.ActionDescriptor;
import control.arguments.BehaviorDescriptor;
import factory.agent.action.ActionDescriptorFactory;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public abstract class BehaviorDescriptorFactory {

    public static BehaviorDescriptor instantiate(Element e, LayerManager layerManager, GeneralParameters p) {

        ActionDescriptor[] actionSequence = getActionSequence(e, layerManager, p);

        Function<BehaviorCell, Behavior> fn = cell -> {
            Action[] actions = instantiateAll(actionSequence, cell);
            return new Behavior(cell, layerManager, actions);
        };
        return new BehaviorDescriptor(fn);
    }

    private static ActionDescriptor[] getActionSequence(Element e, LayerManager layerManager, GeneralParameters p) {
        List<Object> elements = e.elements();
        ActionDescriptor[] ret = new ActionDescriptor[elements.size()];
        elements.stream()
                .map(o -> (Element) o)
                .map(actionElem -> ActionDescriptorFactory.instantiate(actionElem, layerManager, p))
                .collect(Collectors.toList()).toArray(ret);

        return ret;
    }

    private static Action[] instantiateAll(ActionDescriptor[] descriptors, BehaviorCell cell) {
        Action[] ret = new Action[descriptors.length];

        Arrays.asList(descriptors)
                .stream()
                .map(descriptor -> descriptor.instantiate(cell))
                .collect(Collectors.toList()).toArray(ret);

        return ret;
    }
}
