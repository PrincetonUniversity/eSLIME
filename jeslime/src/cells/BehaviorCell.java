package cells;

import agent.control.BehaviorDispatcher;
import structural.identifiers.Coordinate;

/**
 * Flexible cell class capable of performing arbitrary
 * bottom-up (agent-driven) behaviors for agent-based
 * modeling.
 *
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCell extends Cell {

    private int considerCount;
    private double nextFitness;
    private double threshold;
    private BehaviorDispatcher dispatcher;


    public BehaviorCell(int state, double initialFitness, double threshold) {
        this.threshold = threshold;

        setState(state);
        setFitness(initialFitness);
        checkDivisibility();
        considerCount = 0;
    }

    @Override
    public int consider() {
        considerCount++;
        return considerCount;
    }

    @Override
    public void apply() {
        setFitness(nextFitness);
        checkDivisibility();
        considerCount = 0;
    }

    @Override
    public Cell divide() {
        if (!isDivisible()) {
            throw new IllegalStateException("Attempted to divide non-divisible cell.");
        }

        BehaviorCell daughter = new BehaviorCell(getState(), getFitness() / 2, threshold);
        double halfFitness = getFitness() / 2.0D;
        setFitness(halfFitness);
        daughter.setDispatcher(dispatcher.clone(daughter));
        checkDivisibility();
        return daughter;
    }

    @Override
    public Cell clone(int childState) {
        double fitness = getFitness();

        BehaviorCell child = new BehaviorCell(childState, fitness, threshold);
        child.considerCount = considerCount;
        child.nextFitness = nextFitness;
        child.setDispatcher(dispatcher.clone(child));
        child.setDivisible(isDivisible());

        return child;
    }

    @Override
    public double getProduction(String solute) {
        throw new UnsupportedOperationException("BehaviorCell does not yet support diffusible solutes.");
    }

    @Override
    public void feed(double delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        dispatcher.trigger(behaviorName, caller);
    }

    private void checkDivisibility() {
        //System.out.println("   " + getFitness() + " -- " + threshold);
        if (getFitness() > threshold) {
            setDivisible(true);
        } else {
            setDivisible(false);
        }
    }

    public void setDispatcher(BehaviorDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * A BehaviorCell is equal to another Object if and only if:
     *    - The other Object is a BehaviorCell.
     *    - The other Object has a dispatcher that reports equality
     *      with this cell's dispatcher.
     *
     * @see agent.control.BehaviorDispatcher
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BehaviorCell)) {
            return false;
        }

        BehaviorCell other = (BehaviorCell) obj;

        return other.dispatcher.equals(this.dispatcher);
    }
}
