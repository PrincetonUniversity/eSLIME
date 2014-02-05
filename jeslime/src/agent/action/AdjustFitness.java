package agent.action;

import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import structural.EpsilonUtil;
import structural.identifiers.Coordinate;

/**
 * Adjusts the agent's fitness by a predefined delta.
 *
 * Created by David B Borenstein on 2/5/14.
 */
public class AdjustFitness extends Action {

    private double delta;

    public AdjustFitness(BehaviorCell callback, LayerManager layerManager, double delta) {
        super(callback, layerManager);
        this.delta = delta;
    }

    @Override
    public void run(Coordinate caller) {
        BehaviorCell cell = getCallback();
        double curFitness = cell.getFitness();
        double adjFitness = curFitness + delta;
        cell.setFitness(adjFitness);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  AdjustFitness)) {
            return false;
        }

        AdjustFitness other = (AdjustFitness) obj;
        if (!EpsilonUtil.epsilonEquals(other.delta, this.delta)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        AdjustFitness clone = new AdjustFitness(child, getLayerManager(), delta);
        return clone;
    }
}
