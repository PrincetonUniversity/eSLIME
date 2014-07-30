/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/manuscript/figure4/AvP_immune_2D_sweep_sample.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/manuscript/figure3/AvP_immune_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/manuscript/figure3/AvP_non-immune_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/manuscript/figure3/AvP_non-immune_no-glyphs_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-07-08/2D_test.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-07-08/3D_test.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-06/0.50_0.70.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-05/AvA_RE_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-05/AvP_kymograph.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-04/growth_ivp_range_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-03/AvA/0.50_1.00.xml";
        //String path = "/Users/dbborens/projects/T6SS/scripts/AvA/range_2d/0.05_0.40.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-29/growth_ivpi_arena_halt_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-29/growth_ivpi_range_arena_1D.xml";

//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-26/swap_only.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-26/growth_ivpk_arena_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-26/growth_ivps_arena_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-26/growth_ivpks_arena_1D.xml";

//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/growth_ivp_arena_huge.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/db_ivp_periodic_huge.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/growth_ivp_arena_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-06-01/growth_ivi_2d.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/growth_ivps_arena_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/growth_ivps_arena_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/db_ivpk_periodic_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/db_ivpk_periodic_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/db_ivps_periodic_1D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/db_ivps_periodic_2D.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/2014-05-23/growth_ivpks_arena_1D.xml";


//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/pacifist_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/correlation_test.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/debug.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_pacifist_lite.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/deathbirth/db_instigator_vs_pacifist_periodic.xml";

//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/deathbirth/db_instigator_vs_pacifist_linear.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/pacifist_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/basler/cholera_vs_ecoli.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/instigator_vs_pacifist_old_paradigm.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/growth_linear.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/instigator_vs_pacifist_linear.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/instigator_vs_instigator.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/simplified/instigator_vs_pacifist_variant.xml";

//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/deathbirth/db_instigator_vs_pacifist_arena.xml";
//        String p
// ath = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_instigator.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/retaliator_vs_retaliator.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/retaliator_vs_pacifist.xml";
//        String path = "/Users/dbborens/IdeaProjects/t6ss/xml/instigator_vs_retaliator.xml";
        Runner runner = new Runner(path);
        runner.run();

    }

}
