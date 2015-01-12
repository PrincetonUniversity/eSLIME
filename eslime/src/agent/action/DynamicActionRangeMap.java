/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;
import layers.LayerManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbborens on 4/27/14.
 */
public class DynamicActionRangeMap {

    private Map<Action, ProbabilitySupplier> functionMap;
    private ActionRangeMap valueMap;
    private LayerManager layerManager;

    public DynamicActionRangeMap(LayerManager layerManager) {
        functionMap = new HashMap<>();
        this.layerManager = layerManager;
    }

    public void add(Action action, ProbabilitySupplier supplier) {
        functionMap.put(action, supplier);
    }

    public void refresh() {
        valueMap = new ActionRangeMap(functionMap.size());
        functionMap.forEach((action, supplier) -> {
            double value = supplier.get();
            valueMap.add(action, value);
        });
    }

    public Action selectTarget(double x) {
        return valueMap.selectTarget(x);
    }

    public double getTotalWeight() {
        return valueMap.getTotalWeight();
    }

    public DynamicActionRangeMap clone(BehaviorCell child) {
        DynamicActionRangeMap cloned = new DynamicActionRangeMap(layerManager);

        functionMap.forEach((action, supplier) -> {
            Action clonedKey = action.clone(child);
            ProbabilitySupplier clonedValue = supplier.clone(child);
            cloned.add(clonedKey, clonedValue);
        });

        return cloned;
    }
}
