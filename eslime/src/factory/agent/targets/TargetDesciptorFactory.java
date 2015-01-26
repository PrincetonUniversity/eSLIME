/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.targets;

import agent.targets.*;
import cells.BehaviorCell;
import control.GeneralParameters;
import control.arguments.TargetDescriptor;
import factory.processes.discrete.filter.FilterFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;

import java.util.function.Function;

/**
 * Created by dbborens on 2/10/14.
 */
public abstract class TargetDesciptorFactory {
    public static TargetDescriptor instantiate(LayerManager layerManager, Element descriptor, GeneralParameters p) {
        String targetName = getName(descriptor);
        int maximum = getMaximum(descriptor);
        Filter filter = getFilter(descriptor, layerManager, p);
        if (targetName.equalsIgnoreCase("all-neighbors")) {
            Function<BehaviorCell, TargetAllNeighbors> fn = cell -> new TargetAllNeighbors(cell, layerManager, filter, maximum, p.getRandom());
            return new TargetDescriptor(fn);
        } else if (targetName.equalsIgnoreCase("occupied-neighbors")) {
            Function<BehaviorCell, TargetOccupiedNeighbors> fn = cell -> new TargetOccupiedNeighbors(cell, layerManager, filter, maximum, p.getRandom());
            return new TargetDescriptor(fn);

        } else if (targetName.equalsIgnoreCase("vacant-neighbors")) {
            Function<BehaviorCell, TargetVacantNeighbors> fn = cell -> new TargetVacantNeighbors(cell, layerManager, filter, maximum, p.getRandom());
            return new TargetDescriptor(fn);
        } else if (targetName.equalsIgnoreCase("caller")) {
            Function<BehaviorCell, TargetCaller> fn = cell -> new TargetCaller(cell, layerManager, filter, maximum, p.getRandom());
            return new TargetDescriptor(fn);

        } else if (targetName.equalsIgnoreCase("self")) {
            Function<BehaviorCell, TargetSelf> fn = cell -> new TargetSelf(cell, layerManager, filter, maximum, p.getRandom());
            return new TargetDescriptor(fn);

        } else {
            throw new IllegalArgumentException("Unrecognized target '" + targetName + "'");
        }
    }

    private static Filter getFilter(Element descriptor, LayerManager layerManager, GeneralParameters p) {
        Element filterElem = descriptor.element("filters");
        if (filterElem == null) {
            return new NullFilter();
        }

        return FilterFactory.instantiate(filterElem, layerManager, p);
    }

    private static int getMaximum(Element descriptor) {
        Element maxElement = descriptor.element("max");

        // Default is no maximum
        if (maxElement == null) {
            return -1;
        }

        String maxStr = maxElement.getTextTrim();
        int maximum = Integer.valueOf(maxStr);
        return maximum;
    }

    private static String getName(Element descriptor) {
        Element classNameElement = descriptor.element("class");
        String name = classNameElement.getTextTrim();

        return name;
    }
}
