/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import agent.control.BehaviorDispatcher;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;
import structural.utilities.EpsilonUtil;

/**
 * Flexible cell class capable of performing arbitrary
 * bottom-up (agent-driven) behaviors for agent-based
 * modeling.
 * <p/>
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCell extends Cell {

    // State
    private int considerCount;
    private double nextHealth;
    private double threshold;

    // Helpers
    private BehaviorDispatcher dispatcher;
    private CallbackManager callbackManager;
    private RelationshipManager relationshipManager;

    // Default constructor for testing
    @Deprecated
    public BehaviorCell() {
    }

    public BehaviorCell(LayerManager layerManager, int state, double initialHealth, double threshold) throws HaltCondition {
        this.threshold = threshold;
        callbackManager = new CallbackManager(this, layerManager);

        setState(state);

        // We use the superclass setHealth here so it doesn't try to update
        // the location, as the cell is usually created before being placed.
        setHealth(initialHealth);

        considerCount = 0;

        SelfLocator locator = () -> callbackManager.getMyLocation();
        relationshipManager = new RelationshipManager(locator);
    }

    @Override
    public int consider() {
        considerCount++;
        return considerCount;
    }

    @Override
    public void apply() {
//        setHealth(nextHealth);
//        checkDivisibility();
        considerCount = 0;
    }

    @Override
    public Cell divide() throws HaltCondition {
        if (!isDivisible()) {
            throw new IllegalStateException("Attempted to divide non-divisible cell.");
        }

        LayerManager layerManager = callbackManager.getLayerManager();
        BehaviorCell daughter = new BehaviorCell(layerManager, getState(), getHealth() / 2, threshold);
        double halfHealth = getHealth() / 2.0D;
        setHealth(halfHealth);
        daughter.setDispatcher(dispatcher.clone(daughter));
        checkDivisibility();
        return daughter;
    }

    @Override
    public BehaviorCell clone(int childState) throws HaltCondition {
        double health = getHealth();

        LayerManager layerManager = callbackManager.getLayerManager();
        BehaviorCell child = new BehaviorCell(layerManager, childState, health, threshold);
        child.considerCount = considerCount;
        child.nextHealth = nextHealth;
        child.setDispatcher(dispatcher.clone(child));
        child.setDivisible(isDivisible());

        return child;
    }

    @Override
    public double getProduction(String solute) {
        throw new UnsupportedOperationException("BehaviorCell does not yet support diffusible solutes.");
    }

    @Override
    public void adjustHealth(double delta) throws HaltCondition {
        double current = getHealth();
        double next = current + delta;
//        System.out.println("      Adjusting health of cell at " + callbackManager.getMyLocation() + " from " + current + " to " + next);
        setHealth(next);
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) throws HaltCondition {
        dispatcher.trigger(behaviorName, caller);
    }

    @Override
    public void die() {
        callbackManager.die();
    }

    @Override
    protected void setDivisible(boolean divisible) throws HaltCondition {
        super.setDivisible(divisible);
        callbackManager.refreshDivisibility();
    }

    private void checkDivisibility() throws HaltCondition {
        //System.out.println("   " + getHealth() + " -- " + threshold);
        if (getHealth() > threshold) {
            setDivisible(true);
        } else {
            setDivisible(false);
        }
    }

    public void setDispatcher(BehaviorDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void setHealth(double health) throws HaltCondition {
        super.setHealth(health);
        checkDivisibility();
    }

    /**
     * A BehaviorCell is equal to another Object if and only if:
     * - The other Object is a BehaviorCell.
     * - The other Object has a division threshold equal to this one.
     * - The other Object has a state ID equal to this one.
     * - The other Object has a dispatcher that reports equality
     * with this cell's dispatcher.
     *
     * @see agent.control.BehaviorDispatcher
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof BehaviorCell)) {
            return false;
        }

        BehaviorCell other = (BehaviorCell) obj;

        if (other.getState() != this.getState()) {
            return false;
        }

        if (!EpsilonUtil.epsilonEquals(this.threshold, other.threshold)) {
            return false;
        }

        return other.dispatcher.equals(this.dispatcher);
    }

    public double getThreshold() {
        return threshold;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }
}
