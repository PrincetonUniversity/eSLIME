/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.run;

import control.GeneralParameters;
import control.Integrator;
import control.ProcessManager;
import control.halt.HaltCondition;
import factory.geometry.GeometryFactory;
import factory.layers.LayerManagerFactory;
import factory.processes.ProcessFactory;
import factory.io.serialize.SerializationFactory;
import io.loader.ProcessLoader;
import io.loader.ProjectLoader;
import io.serialize.SerializationManager;
import layers.LayerManager;
import org.dom4j.Element;

import java.io.File;

/**
 * @author David Bruce Borenstein
 * @test (not needed)
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
        GeometryFactory gm;
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
            gm = new GeometryFactory(pp.getElement("geometry"));
            Element writers = pp.getElement("writers");
            mgr = SerializationFactory.makeManager(writers, p);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        int n = p.getNumInstances();
        for (int i = 0; i < n; i++) {
            lm = LayerManagerFactory.instantiate(pp.getElement("layers"), gm);
            factory = new ProcessFactory(loader, lm, p);
            pm = new ProcessManager(factory, lm);
            Integrator integrator = new Integrator(p, pm, mgr);
            mgr.init(lm);

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
