/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import control.halt.HaltCondition;
import factory.cell.Reaction;
import layers.LayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dbborens on 11/23/14.
 */ public class CellDescriptor extends Argument<Cell> {

    private LayerManager layerManager;

    private Argument<Integer> cellState;

    private Argument<Double> threshold;
    private Argument<Double> initialHealth;

    private List<Reaction> reactions;
    private Map<String, BehaviorDescriptor> behaviorDescriptors;

    public CellDescriptor(LayerManager layerManager) {
        this.layerManager = layerManager;
        reactions = new ArrayList<>(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellDescriptor that = (CellDescriptor) o;

        //if (!behaviorRoot.equals(that.behaviorRoot)) return false;
        if (!cellState.equals(that.cellState)) return false;
        if (!initialHealth.equals(that.initialHealth)) return false;
        if (!threshold.equals(that.threshold)) return false;

        return true;
    }

    @Override
    public BehaviorCell next() throws HaltCondition {
        // Load cell properties
        double initialHealthValue = initialHealth.next();
        double thresholdValue = threshold.next();
        int stateValue = cellState.next();

        // Construct cell
        BehaviorCell cell = new BehaviorCell(layerManager, stateValue, initialHealthValue, thresholdValue);

        loadReactions(cell);
        loadBehaviors(cell);
        // Return completed object
        return cell;
    }

    public void setCellState(Argument<Integer> cellState) {
        this.cellState = cellState;
    }

    public void setThreshold(Argument<Double> threshold) {
        this.threshold = threshold;
    }

    public void setInitialHealth(Argument<Double> initialHealth) {
        this.initialHealth = initialHealth;
    }

    private void loadReactions(BehaviorCell cell) {
        reactions.forEach(cell::load);
    }

    private void loadBehaviors(BehaviorCell cell) {
        BehaviorDispatcher dispatcher = new BehaviorDispatcher();

        behaviorDescriptors.keySet()
                .stream()
                .forEach(name -> {
                    BehaviorDescriptor descriptor = behaviorDescriptors.get(name);
                    Behavior behavior = descriptor.instantiate(cell);
                    dispatcher.map(name, behavior);
                });

        cell.setDispatcher(dispatcher);
    }

    public void setReactions(Stream<Reaction> reactions) {
        this.reactions = reactions.collect(Collectors.toList());
    }

    public void setBehaviorDescriptors(Map<String, BehaviorDescriptor> behaviorDescriptors) {
        this.behaviorDescriptors = behaviorDescriptors;
    }
}
