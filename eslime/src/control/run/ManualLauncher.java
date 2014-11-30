/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.run;

import factory.control.run.RunnerFactory;

/**
 * The manual runner specifies a hard-coded parameters file to be loaded.
 * It is used for ad-hoc simulations and testing. Batch executions use
 * a command line argument to specify a parameters file.
 *
 * @author dbborens
 */
public class ManualLauncher {

    public static void main(String[] args) {
        String path = "/Users/dbborens/Downloads/minimal.xml";
        Runner runner = RunnerFactory.instantiate(path);
        runner.run();
    }

}
