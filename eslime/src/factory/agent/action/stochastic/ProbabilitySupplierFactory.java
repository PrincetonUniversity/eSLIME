/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action.stochastic;

import agent.action.stochastic.ConstantProbabilitySupplier;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class ProbabilitySupplierFactory {

    public static ProbabilitySupplier instantiate(Element option, BehaviorCell cell, LayerManager layerManager) {
        Element weight = option.element("weight");
        if (weight == null) {
            throw new IllegalArgumentException("Missing required argument 'weight'");
        }
        if (weight.elements().size() == 0) {
            double value = Double.valueOf(weight.getTextTrim());
            return new ConstantProbabilitySupplier(value);
        } else {
            return DependentProbabilitySupplierFactory.instantiate(option, cell, layerManager);
        }
    }
}
