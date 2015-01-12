/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.action.stochastic;

import agent.action.stochastic.DependentProbabilitySupplier;
import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import control.identifiers.Coordinate;
import layers.LayerManager;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class DependentProbabilitySupplierFactory {
    public static ProbabilitySupplier instantiate(Element option, BehaviorCell cell, LayerManager layerManager) {

        String fieldName = XmlUtil.getString(option, "layer");
        double coefficient = XmlUtil.getDouble(option, "coefficient", 1.0);
        double offset = XmlUtil.getDouble(option, "offset", 0.0);
        Function<BehaviorCell, Double> valueLookup = c -> getFieldValueAt(c, layerManager, fieldName);
        return new DependentProbabilitySupplier(valueLookup, cell, coefficient, offset);
    }

    private static double getFieldValueAt(BehaviorCell cell, LayerManager layerManager, String fieldName) {

        Supplier<Coordinate> supplier = () -> layerManager
                .getCellLayer()
                .getLookupManager()
                .getCellLocation(cell);

        double value = layerManager.getContinuumLinker(fieldName).get(supplier);
        return value;
    }
}
