/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package agent.action;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.Chooser;
import structural.identifiers.Coordinate;

import java.util.Random;

/**
 * Upon trigger, selects one of several actions, each of
 * which has a relative probability that is pre-assigned.
 * The chosen action executes as normal.
 * <p/>
 * Created by dbborens on 3/6/14.
 */
public class StochasticChoice extends Action {
    private Chooser<Action> choices;
    private Random random;

    public StochasticChoice(BehaviorCell callback, LayerManager layerManager,
                            Chooser<Action> choices, Random random) {

        super(callback, layerManager);
        this.choices = choices;
        this.random = random;
    }

    @Override
    public void run(Coordinate caller) {
        double range = choices.getTotalWeight();
        double x = random.nextDouble() * range;
        Action choice = choices.selectTarget(x);
        choice.run(caller);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StochasticChoice)) {
            return false;
        }

        StochasticChoice other = (StochasticChoice) obj;

        if (!choices.equals(other.choices)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        Chooser<Action> clonedChoices = choices.clone();
        StochasticChoice cloned = new StochasticChoice(child, getLayerManager(), clonedChoices, random);
        return cloned;
    }
}
