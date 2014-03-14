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

package control;

import io.project.GeometryManager;
import io.project.ProcessFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;
import io.serialize.SerializationManager;
import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

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
            mgr = new SerializationManager(writers, p, lm);
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
