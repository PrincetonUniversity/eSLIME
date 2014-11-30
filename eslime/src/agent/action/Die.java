/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;

/**
 * Created by dbborens on 2/10/14.
 */
public class Die extends Action {

    private Argument<Integer> channel;
    public Die(BehaviorCell callback, LayerManager layerManager,  Argument<Integer> channel) {
        super(callback, layerManager);
        this.channel = channel;
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        doHighlight(channel, getOwnLocation());
        getCallback().die();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Die) {
            return true;
        }
        return false;
    }

    @Override
    public Action clone(BehaviorCell child) {
        return new Die(child, getLayerManager(), channel);
    }
}
