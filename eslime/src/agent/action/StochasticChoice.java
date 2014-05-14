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

package agent.action;

import cells.BehaviorCell;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;
import structural.RangeMap;

import java.util.Random;

/**
 * Upon trigger, selects one of several actions, each of
 * which has a relative probability that is pre-assigned.
 * The chosen action executes as normal.
 * <p/>
 * Created by dbborens on 3/6/14.
 */
public class StochasticChoice extends Action {
    private ActionRangeMap chooser;
    private Random random;

    public StochasticChoice(BehaviorCell callback, LayerManager layerManager,
                            ActionRangeMap chooser, Random random) {

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

        StochasticChoice other = (StochasticChoice) obj;

        if (!chooser.equals(other.chooser)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        // BUG IS HERE -- need an ActionRangeMap extends RangeMap<Action> that clones actions with new targets
        ActionRangeMap clonedChooser = chooser.clone(child);
        StochasticChoice cloned = new StochasticChoice(child, getLayerManager(), clonedChooser, random);
        return cloned;
    }
}
