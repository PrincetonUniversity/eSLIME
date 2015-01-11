/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;

import java.util.Random;

/**
 * Upon trigger, selects one of several actions, each of
 * which has a relative probability that is pre-assigned.
 * The chosen action executes as normal.
 * <p/>
 * Created by dbborens on 3/6/14.
 */
public class StochasticChoice extends Action {
    private DynamicActionRangeMap chooser;
    private Random random;

    public StochasticChoice(BehaviorCell callback, LayerManager layerManager,
                            DynamicActionRangeMap chooser, Random random) {

        super(callback, layerManager);
        this.chooser = chooser;
        this.random = random;
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        double range = chooser.getTotalWeight();
        double x = random.nextDouble() * range;
        Action choice = chooser.selectTarget(x);
        choice.run(caller);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StochasticChoice)) {
            return false;
        }

        // The equality method here is only used during factory testing.
        // True comparison would be an absolute bear, and it can
        // be completely avoided through the use of mocks. As such,
        // I am disabling this logic here for the moment.

//        StochasticChoice other = (StochasticChoice) obj;
//
//        if (!chooser.equals(other.chooser)) {
//            return false;
//        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        DynamicActionRangeMap clonedChooser = chooser.clone(child);
        StochasticChoice cloned = new StochasticChoice(child, getLayerManager(), clonedChooser, random);
        return cloned;
    }
}