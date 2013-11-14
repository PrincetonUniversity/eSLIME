package control;

import io.project.GeometryFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;
import io.serialize.SerializationManager;

import java.io.File;

import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.postprocess.ImageSequence;
import geometry.Geometry;
import models.*;

/**
 * The manual runner specifies a hard-coded parameters file to be loaded.
 * It is used for ad-hoc simulations and testing. Batch executions use
 * a command line argument to specify a parameters file.
 * 
 * @test (not needed)
 * @author dbborens
 * @see ConsoleLaunch
 */
public class ConsoleLauncher {

	public static void main(String[] args) {
		String path = args[0];
		Runner runner = new Runner(path);
		runner.run();
	}

}
