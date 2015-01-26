/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action;

import agent.action.Action;
import agent.action.DynamicActionRangeMap;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import control.GeneralParameters;
import control.arguments.ActionDescriptor;
import control.arguments.DynamicActionRangeMapDescriptor;
import control.arguments.ProbabilitySupplierDescriptor;
import factory.agent.BehaviorDescriptorFactory;
import factory.agent.action.stochastic.ProbabilitySupplierDescriptorFactory;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Constructs an action chooser, which is used in construting a StochasticChoice
 * object. Each choice has a weighting and one or more actions to be executed in
 * sequence. If more than one action is specified, the actions are executed in
 * the order specified whenever the option is chosen.
 * <p>
 * For examples, see
 * Created by dbborens on 3/6/14.
 */
public abstract class DynamicActionRangeMapDescriptorFactory {

    public static DynamicActionRangeMapDescriptor instantiate(Element base, LayerManager layerManager, GeneralParameters p) {



        Map<ActionDescriptor, ProbabilitySupplierDescriptor> prototypes = new HashMap<>();

        for (Object o : base.elements("option")) {
            Element option = (Element) o;
            ActionDescriptor action = getAction(option, layerManager, p);
            ProbabilitySupplierDescriptor supplier = ProbabilitySupplierDescriptorFactory.instantiate(option, layerManager);
            prototypes.put(action, supplier);
//            chooser.add(action, supplier);
        }

        Function<BehaviorCell, DynamicActionRangeMap> fn = cell -> {
            DynamicActionRangeMap chooser = new DynamicActionRangeMap(layerManager);
            prototypes.keySet()
                    .stream()
                    .forEach(actionDescriptor -> {
                        Action action = actionDescriptor.instantiate(cell);
                        ProbabilitySupplierDescriptor supplierDescriptor = prototypes.get(actionDescriptor);
                        ProbabilitySupplier supplier = supplierDescriptor.instantiate(cell);
                        chooser.add(action, supplier);
                    });
            return chooser;
        };

        return new DynamicActionRangeMapDescriptor(fn);
    }

    private static ActionDescriptor getAction(Element option, LayerManager layerManager, GeneralParameters p) {
        Element actionElement = option.element("action");
        List elements = actionElement.elements();
        if (elements.size() == 0) {
            throw new IllegalArgumentException("Expected an action or list of actions for stochastic choice option.");
        } else if (elements.size() == 1) {
            Element child = (Element) actionElement.elements().iterator().next();
            return ActionDescriptorFactory.instantiate(child, layerManager, p);
        } else {
            return BehaviorDescriptorFactory.instantiate(actionElement, layerManager, p);
        }
    }

}
