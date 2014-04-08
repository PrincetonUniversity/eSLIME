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

package processes.temporal;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.loader.ProcessLoader;
import layers.LayerManager;
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

    public ExponentialInverse(ProcessLoader loader, LayerManager layerManager,
                              Integer id, GeneralParameters p) {

        super(loader, layerManager, id, p);
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        double lambda = 1.0D / layerManager.getCellLayer().getViewer().getOccupiedSites().size();
        double dt = expRandom(lambda);
        state.advanceClock(dt);
    }

}
