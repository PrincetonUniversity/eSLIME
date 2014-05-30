/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package control.halt;

/**
 * Created by dbborens on 5/29/14.
 */
public class PopulationFractionReachedEvent extends HaltCondition {

    private Integer state;

    public PopulationFractionReachedEvent(double gillespie, Integer cellState) {
        super(gillespie);
        this.state = state;
    }

    public Integer getState() {
        return state;
    }
}
