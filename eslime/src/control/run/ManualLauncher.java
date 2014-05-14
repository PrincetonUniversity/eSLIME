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

package control.run;

/**
 * The manual runner specifies a hard-coded parameters file to be loaded.
 * It is used for ad-hoc simulations and testing. Batch executions use
 * a command line argument to specify a parameters file.
 *
 * @author dbborens
 */
public class ManualLauncher {

    public static void main(String[] args) {
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/pacifist_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/correlation_test.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/debug.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_pacifist_lite.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/deathbirth/db_instigator_vs_pacifist_periodic.xml";
        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/deathbirth/db_instigator_vs_pacifist_arena.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_instigator.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/retaliator_vs_retaliator.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/retaliator_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_retaliator.xml";
        Runner runner = new Runner(path);
        runner.run();

    }

}
