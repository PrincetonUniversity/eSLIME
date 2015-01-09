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
import factory.agent.BehaviorFactory;
import factory.agent.action.stochastic.ProbabilitySupplierFactory;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.List;

/**
 * Constructs an action chooser, which is used in construting a StochasticChoice
 * object. Each choice has a weighting and one or more actions to be executed in
 * sequence. If more than one action is specified, the actions are executed in
 * the order specified whenever the option is chosen.
 * <p>
 * For examples, see
 * Created by dbborens on 3/6/14.
 */
public abstract class DynamicActionRangeMapFactory {

    public static DynamicActionRangeMap instantiate(Element base, BehaviorCell callback, LayerManager layerManager,
                                                    GeneralParameters p) {

        DynamicActionRangeMap chooser = new DynamicActionRangeMap(layerManager);

        for (Object o : base.elements("option")) {
            Element option = (Element) o;
            Action action = getAction(option, callback, layerManager, p);
            ProbabilitySupplier supplier = ProbabilitySupplierFactory.instantiate(option, callback, layerManager);
            chooser.add(action, supplier);
        }

        return (chooser);
    }

    private static Action getAction(Element option, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        List elements = option.elements();
        if (elements.size() == 0) {
            throw new IllegalArgumentException("Expected an action or list of actions for stochastic choice option.");
        } else if (elements.size() == 1) {
            Element child = (Element) option.elements().iterator().next();
            return ActionFactory.instantiate(child, callback, layerManager, p);
        } else {
            return BehaviorFactory.instantiateAsCompoundAction(option, callback, layerManager, p);
        }
    }

}
