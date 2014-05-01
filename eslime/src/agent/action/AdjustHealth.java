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
import control.identifiers.Coordinate;
import layers.LayerManager;
import structural.utilities.EpsilonUtil;

/**
 * Adjusts the agent's health by a predefined delta.
 * <p/>
 * Created by David B Borenstein on 2/5/14.
 */
public class AdjustHealth extends Action {

    private double delta;

    public AdjustHealth(BehaviorCell callback, LayerManager layerManager, double delta) {
        super(callback, layerManager);
        this.delta = delta;
    }

    @Override
    public void run(Coordinate caller) {
        BehaviorCell cell = getCallback();
        double curHealth = cell.getHealth();
        double adjHealth = curHealth + delta;
        cell.setHealth(adjHealth);

//        // DEBUG CODE
//        Coordinate self = getLayerManager().getCellLayer().getLookupManager().getCellLocation(cell);
//        System.out.println("Adjusted cell at " + self + " from " + curHealth + " to " + adjHealth);
//        System.out.println("   Is this cell now divisible? " + cell.isDivisible());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AdjustHealth)) {
            return false;
        }

        AdjustHealth other = (AdjustHealth) obj;
        if (!EpsilonUtil.epsilonEquals(other.delta, this.delta)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        AdjustHealth clone = new AdjustHealth(child, getLayerManager(), delta);
        return clone;
    }
}
