/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;

import control.GeneralParameters;
import control.arguments.*;
import factory.agent.BehaviorDescriptorFactory;
import factory.cell.Reaction;
import factory.cell.ReactionFactory;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class CellDescriptorFactory {

    private static final int DEFAULT_STATE = 1;
    private static final double DEFAULT_INIT_HEALTH = 0.5;
    private static final double DEFAULT_THRESHOLD = 1.0;

    public static CellDescriptor instantiate(Element e, LayerManager layerManager, GeneralParameters p) {
        if (e == null) {
            return makeDefault(layerManager, p);
        }
        CellDescriptor cellDescriptor = new CellDescriptor(layerManager);
        setCellState(e, cellDescriptor, p.getRandom());
        setThreshold(e, cellDescriptor, p.getRandom());
        setInitialHealth(e, cellDescriptor, p.getRandom());
        loadReactions(e, cellDescriptor);
        loadBehaviors(e, cellDescriptor, layerManager, p);

        return cellDescriptor;
    }

    private static void loadBehaviors(Element e, CellDescriptor cellDescriptor, LayerManager layerManager, GeneralParameters p) {
        Element behaviorElem = e.element("behaviors");

        // No behaviors? No problem.
        if (behaviorElem == null) {
            cellDescriptor.setBehaviorDescriptors(new HashMap<>(0));
            return;
        }

        List<Object> elements = behaviorElem.elements();
        HashMap<String, BehaviorDescriptor> behaviorDescriptors = new HashMap<>(elements.size());
        elements.stream()
                .map(o -> (Element) o)
                .forEach(element -> {
                    String name = element.getName();

                    BehaviorDescriptor behaviorDescriptor = BehaviorDescriptorFactory.instantiate(element, layerManager, p);
                    behaviorDescriptors.put(name, behaviorDescriptor);
                });

        cellDescriptor.setBehaviorDescriptors(behaviorDescriptors);
    }



    private static void loadReactions(Element e, CellDescriptor cellDescriptor) {
        Element reactions = e.element("reactions");
        if (reactions == null) {
            return;
        }

        List<Object> elements = reactions.elements();

        Stream<Reaction> reactionStream = elements.stream()
                .map(x -> (Element) x)
                .map(ReactionFactory::instantiate);

        cellDescriptor.setReactions(reactionStream);
    }

    public static CellDescriptor makeDefault(LayerManager layerManager, GeneralParameters p) {
        CellDescriptor cellDescriptor = new CellDescriptor(layerManager);
        cellDescriptor.setCellState(new ConstantInteger(DEFAULT_STATE));
        cellDescriptor.setThreshold(new ConstantDouble(DEFAULT_THRESHOLD));
        cellDescriptor.setInitialHealth(new ConstantDouble(DEFAULT_INIT_HEALTH));
        return cellDescriptor;
    }

    private static void setInitialHealth(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Double> initialHealth = DoubleArgumentFactory.instantiate(e, "initial-health", DEFAULT_INIT_HEALTH, random);
        cellDescriptor.setInitialHealth(initialHealth);
    }

    private static void setThreshold(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Double> threshold = DoubleArgumentFactory.instantiate(e, "threshold", DEFAULT_THRESHOLD, random);
        cellDescriptor.setThreshold(threshold);
    }

    private static void setCellState(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Integer> cellState = IntegerArgumentFactory.instantiate(e, "state", DEFAULT_STATE, random);
        cellDescriptor.setCellState(cellState);
    }


}
