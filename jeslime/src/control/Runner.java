package control;

import io.project.GeometryManager;
import io.project.ProcessFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;
import io.serialize.SerializationManager;

import java.io.File;

import layers.LayerManager;
import org.dom4j.Element;

import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * 
 * @test (not needed)
 * @author David Bruce Borenstein
 *
 */
public class Runner implements Runnable {

	private String fn;
	public Runner(String fn) {
		this.fn = fn;
	}

	public void run() {

        // The runner is building too many objects. These are
        // mostly invoked once in a hierarchical build process.
        // Is there a good pattern for this?
		GeneralParameters p;
		GeometryManager gm;
		ProjectLoader pp;
		ProcessLoader loader;
        LayerManager lm;
        ProcessManager pm;
        ProcessFactory factory;
        SerializationManager mgr;
		try {
			File f = new File(fn);
			pp = new ProjectLoader(f);
			Element processRoot = pp.getElement("cell-processes");
			loader = new ProcessLoader(processRoot);
			p = new GeneralParameters(pp);
			gm = new GeometryManager(pp.getElement("geometry"));
            lm = new LayerManager(pp.getElement("layers"), gm);
            factory = new ProcessFactory(loader, lm, p);
            pm = new ProcessManager(factory, p);
            Element writers = pp.getElement("writers");
            mgr  = new SerializationManager(writers, p, lm);
            mgr.init(lm);
        } catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		for (int i = 0; i < p.getNumInstances(); i++) {
		
			Integrator integrator = new Integrator(p, pm, mgr);
			
			// This step includes the execution of the simulation until some
			// end condition(s) defined in the parameters. It includes setting up
			// the initial conditions.
			HaltCondition ex = integrator.go();

			// This step includes any analysis or visualizations that take place
			// after the integrator is done running. It also closes any open handles
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
