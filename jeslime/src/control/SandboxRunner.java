package control;

import models.*;

/**
 * Use to create arbitrary entry points during testing and development.
 * 
 * @author dbborens
 *
 */
public class SandboxRunner implements Runnable {

	public static void main(String[] args) {
		SandboxRunner runner = new SandboxRunner();
		runner.run();
	}

	@Override
	public void run() {
		Parameters p = new Parameters();
		Model model = new ExponentialGrowth(p);
		model.initialize();
		model.go();
	}
}
