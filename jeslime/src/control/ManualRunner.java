package control;

import io.parameters.GeometryFactory;
import io.parameters.ParameterLoader;
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
 *
 */
public class ManualRunner implements Runnable {

	public static void main(String[] args) {
		ManualRunner runner = new ManualRunner();
		runner.run();
	}

	@Override
	public void run() {
		
		GeneralParameters p = null;
		Geometry g;
		try {
			File f = new File("/Users/dbborens/github/jeSLIME/jeslime/projects/sandbox.xml");
			ParameterLoader pp = new ParameterLoader(f);
			p = new GeneralParameters(pp);
			g = GeometryFactory.make(pp);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		SerializationManager mgr = new SerializationManager(p, g);
		
		for (int i = 0; i < p.getNumInstances(); i++) {
			
			Model model = new ExponentialGrowth(p, g, mgr);
			
			// This step includes setting up the initial conditions, as well as
			// some data structure initializations.
			model.initialize();
			
			// This step includes the execution of the simulation until some
			// end condition(s) defined in the parameters.
			HaltCondition ex = model.go();

			// Instructs the model to perform any final cleanup actions on behalf
			// of its various processes.
			model.postprocess(ex);

			// This step includes any analysis or visualizations that take place
			// after the model is done running. It also closes any open handles
			// or managers instantiated by the serialization manager on behalf
			// of the instance.
			mgr.dispatchHalt(ex);
			
			// This instructs the parameter handler to re-initialize the random
			// number generator and to update paths to reflect the next 
			// iterate. It is only invoked if there are remaining iterates.
			if (i < p.getNumInstances() - 1) {
				p.advance();
			}
		}
		
		mgr.close();
		
	}
}
