/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.temporal;

import control.halt.HaltCondition;
import processes.BaseProcessArguments;
import processes.StepState;

/**
 * Simple stochastic process for time scaling. Assumes all cells
 * are growing at the same rate: 1 cell per arbitrary unit. So
 * choose an exponentially distributed random number whose lambda
 * is inversely proportional to the number of cells. This is
 * equivalent to a Gillespie process in the case where cell division
 * is the only process and where all cells have an equal
 * probability of dividing.
 *
 * @author dbborens
 */
public class ExponentialInverse extends TimeProcess {

    public ExponentialInverse(BaseProcessArguments arguments) {
        super(arguments);
    }

    @Override
    public void init() {
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        double lambda = 1.0D / layerManager.getCellLayer().getViewer().getOccupiedSites().size();
        double dt = expRandom(lambda);
        state.advanceClock(dt);
    }

}
