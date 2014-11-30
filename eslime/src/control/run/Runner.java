/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.run;

import control.GeneralParameters;
import control.Integrator;

/**
 * Created by dbborens on 11/26/14.
 */
public class Runner implements Runnable {

    private GeneralParameters p;
    private Integrator integrator;

    public Runner(GeneralParameters p, Integrator integrator) {
        this.p = p;
        this.integrator = integrator;
    }

    public void run() {
        int n = p.getNumInstances();
        for (int i = 0; i < n; i++) {
            integrator.doNext();

            // This instructs the parameter handler to re-initialize the random
            // number generator and to update paths to reflect the next
            // iterate. It is only invoked if there are remaining iterates.
            if (i < p.getNumInstances() - 1) {
                p.advance();
            }
        }
    }
}
