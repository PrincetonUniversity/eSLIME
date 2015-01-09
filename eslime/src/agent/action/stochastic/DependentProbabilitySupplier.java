/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action.stochastic;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import layers.LayerManager;

/**
 * This is part of a cloodge to make it so that StochasticChoice can read the state
 * of solute fields. As such, it is not very carefully written. This will be replaced
 * when more flexible state objects are made available.
 * <p>
 * Created by dbborens on 1/9/15.
 */
public class DependentProbabilitySupplier extends ProbabilitySupplier {

    private double coefficient;
    private double offset;
    private LayerManager layerManager;
    private BehaviorCell cell;
    private String fieldName;

    public DependentProbabilitySupplier(LayerManager layerManager, BehaviorCell cell, String fieldName, double coefficient, double offset) {
        this.coefficient = coefficient;
        this.offset = offset;
        this.layerManager = layerManager;
        this.cell = cell;
        this.fieldName = fieldName;
    }

    private static double getFieldValueAt(BehaviorCell cell, LayerManager layerManager, String fieldName) {
        Coordinate c = layerManager
                .getCellLayer()
                .getLookupManager()
                .getCellLocation(cell);

        double value = layerManager.getLinker(fieldName).get(c);
        return value;
    }

    @Override
    public ProbabilitySupplier clone(BehaviorCell child) {
        return new DependentProbabilitySupplier(layerManager, child, fieldName, coefficient, offset);
    }

    @Override
    public Double get() {
        double value = getFieldValueAt(cell, layerManager, fieldName);
        return (coefficient * value) + offset;
    }
}
