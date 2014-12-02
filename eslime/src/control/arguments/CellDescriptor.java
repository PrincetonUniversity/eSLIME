/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import control.GeneralParameters;
import control.halt.HaltCondition;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 11/23/14.
 */
public class CellDescriptor extends Argument<Cell> {

    private LayerManager layerManager;
    private GeneralParameters p;

    private Argument<Integer> cellState;


    private Argument<Double> threshold;
    private Argument<Double> initialHealth;

    // TODO Remove non-compliant XML handling (83330654)
    private Element behaviorRoot;

    public CellDescriptor(LayerManager layerManager, GeneralParameters p) {
        this.layerManager = layerManager;
        this.p = p;
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
    public Cell next() throws HaltCondition {
        // Load cell properties
        double initialHealthValue = initialHealth.next();
        double thresholdValue = threshold.next();
        int stateValue = cellState.next();

        // Construct cell
        BehaviorCell cell = new BehaviorCell(layerManager, stateValue, initialHealthValue, thresholdValue);

        // Load behaviors
        BehaviorDispatcher dispatcher = new BehaviorDispatcher(behaviorRoot, cell, layerManager, p);
        cell.setDispatcher(dispatcher);

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

    @Deprecated
    public void setBehaviorRoot(Element behaviorRoot) {
        this.behaviorRoot = behaviorRoot;
    }

}
