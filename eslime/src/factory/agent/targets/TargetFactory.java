/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.agent.targets;

import agent.targets.*;
import cells.BehaviorCell;
import control.GeneralParameters;
import factory.processes.discrete.filter.FilterFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;

/**
 * Created by dbborens on 2/10/14.
 */
public abstract class TargetFactory {
    public static TargetRule instantiate(BehaviorCell callback, LayerManager layerManager, Element descriptor, GeneralParameters p) {
        String targetName = getName(descriptor);
        int maximum = getMaximum(descriptor);
        Filter filter = getFilter(descriptor, layerManager, p);
        if (targetName.equalsIgnoreCase("all-neighbors")) {

            return new TargetAllNeighbors(callback, layerManager, filter, maximum, p.getRandom());

        } else if (targetName.equalsIgnoreCase("occupied-neighbors")) {
            return new TargetOccupiedNeighbors(callback, layerManager, filter, maximum, p.getRandom());

        } else if (targetName.equalsIgnoreCase("vacant-neighbors")) {
            return new TargetVacantNeighbors(callback, layerManager, filter, maximum, p.getRandom());

        } else if (targetName.equalsIgnoreCase("caller")) {
            return new TargetCaller(callback, layerManager, filter, maximum, p.getRandom());

        } else if (targetName.equalsIgnoreCase("self")) {
            return new TargetSelf(callback, layerManager, filter, maximum, p.getRandom());

        } else {
            throw new IllegalArgumentException("Unrecognized target '" + targetName + "'");
        }
    }

    private static Filter getFilter(Element descriptor, LayerManager layerManager, GeneralParameters p) {
        Element filterElem = descriptor.element("filter");
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
