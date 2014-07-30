/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.halt.HaltCondition;
import control.identifiers.Coordinate;

/**
 * Parent class for cells.
 *
 * @author David Bruce Borenstein
 */
public abstract class Cell {
    private int state;
    private double health;
    private boolean divisible;

    public int getState() {
        return state;
    }

    public double getHealth() {
        return health;
    }

    public boolean isDivisible() {
        return divisible;
    }

    public abstract int consider();

    /**
     * Applies changes to the cell. DO NOT CALL THIS METHOD DIRECTLY
     * FROM A PROCESS. Instead, use lattice.apply().
     */
    public abstract void apply();

    /**
     * Instructs the cell to divide and returns the daughter cell.
     * This triggers any division-related behaviors.
     * <p/>
     * DO NOT CALL THIS METHOD DIRECTLY
     * FROM A PROCESS. Instead, use lattice.divide().
     *
     * @return
     */
    public abstract Cell divide();

    /**
     * Creates an exact replica of the cell, differing only by the
     * state value. Copies all internal state variables. Does not
     * trigger any division-related events or changes.
     *
     * @param state
     * @return
     */
    public abstract Cell clone(int state);

    /**
     * Returns the current production of the specified solute.
     *
     * @param solute the ID of the solute layer associated with
     *               the solute whose production is to be checked.
     * @return
     */
    public abstract double getProduction(String solute);

    @Override
    public Cell clone() {
        return clone(state);
    }

    /**
     * Informs the cell that it has been given a direct benefit.
     * The effect of this benefit depends on the cell class.
     */
    public abstract void adjustHealth(double delta);

    protected void setState(int state) {
        if (state == 0) {
            throw new IllegalStateException("Attempted to assign special 'dead' state code 0 to an active cell.");
        }

        this.state = state;
    }

    protected void setHealth(double health) {
        this.health = health;
    }

    protected void setDivisible(boolean divisible) {
        this.divisible = divisible;
    }


    /**
     * Triggers a behavior associated with the cell. The specific details of the
     * behavior are defined by the cell's class. As of 1/24/2014, only the
     * BehaviorCell class will have an instantiated trigger(...) method; other
     * classes of cells will throw an UnsupportedOperationException.
     *  @param behaviorName the name of the behavior to trigger.
     * @param caller       the coordinate of the triggering site. If invoked from a
     */
    public abstract void trigger(String behaviorName, Coordinate caller) throws HaltCondition;

    public abstract void die();
}
