package io.project;

import agent.targets.*;
import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 2/10/14.
 */
public abstract class TargetFactory {
    public static Targeter instantiate(BehaviorCell callback, LayerManager layerManager, Element descriptor)  {
        String targetName = descriptor.getTextTrim();

        if (targetName.equalsIgnoreCase("all-neighbors")) {
            return new TargetAllNeighbors(callback, layerManager);

        } else if (targetName.equalsIgnoreCase("occupied-neighbors")) {
            return new TargetOccupiedNeighbors(callback, layerManager);

        } else if  (targetName.equalsIgnoreCase("vacant-neighbors")) {
            return new TargetVacantNeighbors(callback, layerManager);

        } else if (targetName.equalsIgnoreCase("caller")) {
            return new TargetCaller(callback, layerManager);

        } else if (targetName.equalsIgnoreCase("self")) {
            return new TargetSelf(callback, layerManager);

        } else {
            throw new IllegalArgumentException("Unrecognized target '" + targetName + "'");
        }
    }

}
