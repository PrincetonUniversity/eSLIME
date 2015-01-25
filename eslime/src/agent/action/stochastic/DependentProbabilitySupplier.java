/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import layers.LayerManager;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is part of a cloodge to make it so that StochasticChoice can read the state
 * of solute fields. As such, it is not very carefully written. This will be replaced
 * when more flexible state objects are made available.
 *
 * Created by dbborens on 1/9/15.
 */
public class DependentProbabilitySupplier extends ProbabilitySupplier {

    private double coefficient;
    private double offset;
    private BehaviorCell cell;
    private Function<BehaviorCell, Double> valueLookup;

    public DependentProbabilitySupplier(Function<BehaviorCell, Double> valueLookup, BehaviorCell cell, double coefficient, double offset) {
        this.coefficient = coefficient;
        this.offset = offset;
        this.cell = cell;
        this.valueLookup = valueLookup;
    }

    private static double getFieldValueAt(BehaviorCell cell, LayerManager layerManager, String fieldName) {

        Supplier<Coordinate> supplier = () -> layerManager
                .getCellLayer()
                .getLookupManager()
                .getCellLocation(cell);

        double value = layerManager.getContinuumLayer(fieldName).getLinker().get(supplier);
        return value;
    }

    @Override
    public DependentProbabilitySupplier clone(BehaviorCell child) {
        return new DependentProbabilitySupplier(valueLookup, child, coefficient, offset);
    }

    @Override
    public Double get() {
        double value = valueLookup.apply(cell);
        return (coefficient * value) + offset;
    }
}
