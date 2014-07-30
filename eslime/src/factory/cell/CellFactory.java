/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package factory.cell;

import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.ConstantInteger;
import factory.control.arguments.DoubleArgumentFactory;
import factory.control.arguments.IntegerArgumentFactory;
import layers.LayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;

/**
 * Instantiates a cell based on specifications from a cell descriptor
 * XML element. The requirements for this element depend on the class
 * of cell being instantiated. If instantiate() is called numerous times,
 * multiple distinct cells of the same exact specification (include state
 * id) will be returned.
 *
 * @author dbborens
 */
public class CellFactory {

    private LayerManager layerManager;
    private Element cellDescriptor;
    private GeneralParameters p;

    private Argument<Integer> cellState;
    private Argument<Double> threshold;
    private Argument<Double> initialHealth;

    /**
     * @param cellDescriptor
     */
    public CellFactory(LayerManager layerManager, Element cellDescriptor, GeneralParameters p) {
        if (cellDescriptor == null) {
            throw new IllegalArgumentException("A <cell-descriptor> tag was missing where it was expected.");
        }

        this.layerManager = layerManager;
        this.cellDescriptor = cellDescriptor;
        this.p = p;

        cellState = getCellState(cellDescriptor);
        threshold = DoubleArgumentFactory.instantiate(cellDescriptor, "threshold", 2.0, p.getRandom());
        initialHealth = DoubleArgumentFactory.instantiate(cellDescriptor, "initial-health", 1.0, p.getRandom());
    }

    private Argument<Integer> getCellState(Element cellDescriptor) {

        // Check for "sequential" mode
        Element stateElem = cellDescriptor.element("state");
        if (stateElem != null && stateElem.getTextTrim().equalsIgnoreCase("sequential")) {
            int nextState = getNextState();
            return new ConstantInteger(nextState);
        }

        return IntegerArgumentFactory.instantiate(cellDescriptor, "state", p.getRandom());
    }

    public Cell instantiate() {
        String className = cellDescriptor.element("class").getTextTrim();

        if (className.equalsIgnoreCase("BehaviorCell")) {
            return behaviorCell();
        } else {

            String msg = "Unrecognized cell class '" +
                    className + "'.";
            throw new IllegalArgumentException(msg);

        }
    }

    private Cell behaviorCell() {
        // Load cell properties
        double initialHealthValue = initialHealth.next();
        double thresholdValue = threshold.next();
        int stateValue = cellState.next();

        // Construct cell
        BehaviorCell cell = new BehaviorCell(layerManager, stateValue, initialHealthValue, thresholdValue);

        // Load behaviors
        Element behaviorRoot = cellDescriptor.element("behaviors");
        BehaviorDispatcher dispatcher = new BehaviorDispatcher(behaviorRoot, cell, layerManager, p);
        cell.setDispatcher(dispatcher);

        // Return completed object
        return cell;
    }

    private int getNextState() {
        CellLayer layer = layerManager.getCellLayer();
        Integer[] states = layer.getViewer().getStateMapViewer().getStates();
        Integer max = 0;
        for (Integer s : states) {
            if (s > max) {
                max = s;
            }
        }

        return max + 1;
    }
}
