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

package io.factory;

import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import cells.FissionCell;
import cells.SimpleCell;
import layers.LayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import structural.GeneralParameters;

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
    private int state;

    /**
     * @param cellDescriptor
     */
    public CellFactory(LayerManager layerManager, Element cellDescriptor, GeneralParameters p) {
        this.layerManager = layerManager;
        this.cellDescriptor = cellDescriptor;
        this.p = p;
        String stateText = cellDescriptor.element("state").getTextTrim();

        // State should be consistent across all cells instantiated by
        // this factory, even if using a sequential state.
        state = getState(stateText);
    }

    public Cell instantiate() {
        String className = cellDescriptor.element("class").getTextTrim();

        if (className.equalsIgnoreCase("SimpleCell")) {
            return simpleCell(cellDescriptor, state);

        } else if (className.equalsIgnoreCase("FissionCell")) {
            return fissionCell(cellDescriptor, state);

        } else if (className.equalsIgnoreCase("BehaviorCell")) {
            return behaviorCell(cellDescriptor, state);
        } else {

            String msg = "Unrecognized cell class '" +
                    className + "'.";
            throw new IllegalArgumentException(msg);

        }
    }

    private Cell behaviorCell(Element cellDescriptor, int state) {
        // Load cell properties
        double initialFitness = Double.valueOf(cellDescriptor.element("initial-fitness").getText());
        double threshold = Double.valueOf(cellDescriptor.element("threshold").getText());

        // Construct cell
        BehaviorCell cell = new BehaviorCell(layerManager, state, initialFitness, threshold);

        // Load behaviors
        Element behaviorRoot = cellDescriptor.element("behaviors");
        BehaviorDispatcher dispatcher = new BehaviorDispatcher(behaviorRoot, cell, layerManager, p);
        cell.setDispatcher(dispatcher);

        // Return completed object
        return cell;
    }

    private Cell fissionCell(Element cellDescriptor, int state) {
        double initialFitness = Double.valueOf(cellDescriptor.element("initial-fitness").getText());
        double threshold = Double.valueOf(cellDescriptor.element("threshold").getText());

        Cell cell = new FissionCell(state, initialFitness, threshold);

        return cell;
    }

    private Cell simpleCell(Element cellDescriptor, int state) {
        Cell cell = new SimpleCell(state);
        return cell;
    }

    private int getState(String stateText) {
        if (stateText.equalsIgnoreCase("sequential")) {
            return getNextState();
        } else {
            return Integer.valueOf(stateText);
        }
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
