package control;

import java.io.File;

/**
 * The manual runner specifies a hard-coded parameters file to be loaded.
 * It is used for ad-hoc simulations and testing. Batch executions use
 * a command line argument to specify a parameters file.
 * 
 * @author dbborens
 */
public class ManualLauncher {

	public static void main(String[] args) {
		String path = "/Users/dbborens/IdeaProjects/jeSLIME/jeslime/fixtures/AgentRegression.xml";
		Runner runner = new Runner(path);
		runner.run();
	}

}
