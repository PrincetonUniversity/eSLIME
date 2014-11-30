/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;

import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.CellDescriptor;
import control.arguments.ConstantDouble;
import control.arguments.ConstantInteger;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.Random;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class CellDescriptorFactory {

    private static final int DEFAULT_STATE = 1;
    private static final double DEFAULT_INIT_HEALTH = 0.5;
    private static final double DEFAULT_THRESHOLD = 1.0;

    public static CellDescriptor instantiate(Element e, LayerManager layerManager, GeneralParameters p) {
        if (e == null) {
            return makeDefault(layerManager, p);
        }
        CellDescriptor cellDescriptor = new CellDescriptor(layerManager, p);
        setCellState(e, cellDescriptor, p.getRandom());
        setThreshold(e, cellDescriptor, p.getRandom());
        setInitialHealth(e, cellDescriptor, p.getRandom());
        setBehaviorRoot(e, cellDescriptor);
        return cellDescriptor;
    }

    private static CellDescriptor makeDefault(LayerManager layerManager, GeneralParameters p) {
        CellDescriptor cellDescriptor = new CellDescriptor(layerManager, p);
        cellDescriptor.setCellState(new ConstantInteger(DEFAULT_STATE));
        cellDescriptor.setThreshold(new ConstantDouble(DEFAULT_THRESHOLD));
        cellDescriptor.setInitialHealth(new ConstantDouble(DEFAULT_INIT_HEALTH));
        return cellDescriptor;
    }

    private static void setBehaviorRoot(Element e, CellDescriptor cellDescriptor) {
        Element behaviorRoot = e.element("behaviors");
        cellDescriptor.setBehaviorRoot(behaviorRoot);
    }

    private static void setInitialHealth(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Double> initialHealth = DoubleArgumentFactory.instantiate(e, "initial-health", DEFAULT_INIT_HEALTH, random);
        cellDescriptor.setInitialHealth(initialHealth);
    }

    private static void setThreshold(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Double> threshold = DoubleArgumentFactory.instantiate(e, "threshold", DEFAULT_THRESHOLD, random);
        cellDescriptor.setThreshold(threshold);
    }

    private static void setCellState(Element e, CellDescriptor cellDescriptor, Random random) {
        Argument<Integer> cellState = IntegerArgumentFactory.instantiate(e, "state", DEFAULT_STATE, random);
        cellDescriptor.setCellState(cellState);
    }


}
