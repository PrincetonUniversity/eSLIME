/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.temporal;

import control.GeneralParameters;
import processes.BaseProcessArguments;
import processes.EcoProcess;
import processes.gillespie.GillespieState;

/**
 * Convenience superclass for time-specific processes,
 * including some methods for calculating elapsed time.
 * Probably not necessary to have a separate superclass
 * for this.
 * <p/>
 * At the moment, TimeProcess is specific to a particular
 * cell layer.
 *
 * @author David Bruce Borenstein
 * @untested
 */
public abstract class TimeProcess extends EcoProcess {
    protected GeneralParameters p;

    public TimeProcess(BaseProcessArguments arguments) {

        super(arguments);
    }

    public void target(GillespieState gs) {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    /**
     * Returns an exponentially distributed random number.
     */
    protected double expRandom(double lambda) {
        // Get a random number between 0 (inc) and 1 (exc)
        double u = p.getRandom().nextDouble();

        // Inverse of exponential CDF
        return Math.log(1 - u) / (-1 * lambda);
    }
}
