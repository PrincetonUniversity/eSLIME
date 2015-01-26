/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action.stochastic;

import agent.action.stochastic.ConstantProbabilitySupplier;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import control.arguments.ProbabilitySupplierDescriptor;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.function.Function;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class ProbabilitySupplierDescriptorFactory {

    public static ProbabilitySupplierDescriptor instantiate(Element option, LayerManager layerManager) {
        Element weight = option.element("weight");
        if (weight == null) {
            throw new IllegalArgumentException("Missing required argument 'weight'");
        }
        if (weight.elements().size() == 0) {
            double value = Double.valueOf(weight.getTextTrim());
            Function<BehaviorCell, ProbabilitySupplier> fn = cell -> new ConstantProbabilitySupplier(value);
            return new ProbabilitySupplierDescriptor(fn);
        } else {
            return DependentProbabilitySupplierFactory.instantiate(option, layerManager);
        }
    }
}
