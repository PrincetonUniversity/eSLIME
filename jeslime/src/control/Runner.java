package control;

import geometries.Geometry;
import io.project.GeometryFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;
import io.serialize.SerializationManager;

import java.io.File;

import org.dom4j.Element;

import models.Model;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

public class Runner implements Runnable {

	private String fn;
	public Runner(String fn) {
		this.fn = fn;
	}
	
	public void run() {
		
		GeneralParameters p = null;
		Geometry g;
		ProjectLoader pp;
		ProcessLoader loader;
		try {
			File f = new File(fn);
			pp = new ProjectLoader(f);
			Element processRoot = pp.getElement("processes");
			loader = new ProcessLoader(processRoot);
			p = new GeneralParameters(pp);
			g = GeometryFactory.make(pp);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		SerializationManager mgr = new SerializationManager(pp, p, g);
		
		for (int i = 0; i < p.getNumInstances(); i++) {
		
			Model model = new Model(p, loader, g, mgr);
			
			// This step includes the execution of the simulation until some
			// end condition(s) defined in the parameters. It includes setting up
			// the initial conditions.
			HaltCondition ex = model.go();

			// Instructs the model to perform any final cleanup actions on behalf
			// of its various processes.
			model.postprocess(ex);

			// This step includes any analysis or visualizations that take place
			// after the model is done running. It also closes any open handles
			// or managers instantiated by the serialization manager on behalf
			// of the instance.
			mgr.dispatchHalt(ex);
			
			System.out.println(p.getInstancePath());
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
