/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action.stochastic;

import agent.action.stochastic.DependentProbabilitySupplier;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class DependentProbabilitySupplierFactory {
    public static ProbabilitySupplier instantiate(Element option, BehaviorCell cell, LayerManager layerManager) {

        String fieldName = XmlUtil.getString(option, "layer");
        double coefficient = XmlUtil.getDouble(option, "coefficient", 1.0);
        double offset = XmlUtil.getDouble(option, "offset", 0.0);

        return new DependentProbabilitySupplier(layerManager, cell, fieldName, coefficient, offset);
    }
}
