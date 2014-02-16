package io.project;

import agent.targets.*;
import cells.BehaviorCell;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.Random;

/**
 * Created by dbborens on 2/10/14.
 */
public abstract class TargetFactory {
    public static TargetRule instantiate(BehaviorCell callback, LayerManager layerManager, Element descriptor, Random random)  {
        String targetName = getName(descriptor);
        int maximum = getMaximum(descriptor);
        if (targetName.equalsIgnoreCase("all-neighbors")) {

            return new TargetAllNeighbors(callback, layerManager, maximum, random);

        } else if (targetName.equalsIgnoreCase("occupied-neighbors")) {
            return new TargetOccupiedNeighbors(callback, layerManager, maximum, random);

        } else if  (targetName.equalsIgnoreCase("vacant-neighbors")) {
            return new TargetVacantNeighbors(callback, layerManager, maximum, random);

        } else if (targetName.equalsIgnoreCase("caller")) {
            return new TargetCaller(callback, layerManager, maximum, random);

        } else if (targetName.equalsIgnoreCase("self")) {
            return new TargetSelf(callback, layerManager, maximum, random);

        } else {
            throw new IllegalArgumentException("Unrecognized target '" + targetName + "'");
        }
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
