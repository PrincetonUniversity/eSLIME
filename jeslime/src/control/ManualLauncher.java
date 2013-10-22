package control;

import io.project.GeometryFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;
import io.serialize.SerializationManager;

import java.io.File;

import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.postprocess.ImageSequence;
import geometries.Geometry;
import models.*;

/**
 * The manual runner specifies a hard-coded parameters file to be loaded.
 * It is used for ad-hoc simulations and testing. Batch executions use
 * a command line argument to specify a parameters file.
 * 
 * @author dbborens
 * @see ConsoleLaunch
 */
public class ManualLauncher {

	public static void main(String[] args) {
		String path = "/Users/dbborens/github/jeSLIME/jeslime/projects/GeneSurfing.xml";
		Runner runner = new Runner(path);
		runner.run();
	}

}
