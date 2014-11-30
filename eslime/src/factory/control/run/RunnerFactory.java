/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.run;

import control.GeneralParameters;
import control.Integrator;
import control.arguments.GeometryDescriptor;
import control.run.Runner;
import factory.control.IntegratorFactory;
import factory.control.arguments.GeometryDescriptorFactory;
import factory.layers.LayerManagerFactory;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class RunnerFactory {

    // Version -- checked against parameters file to make sure they're
    // compatible
    private final static String VERSION = "0.5.7";

    public static Runner instantiate(String projectPath) {
        Element root = DocumentFactory.instantiate(projectPath);
        validate(root);
        GeneralParameters p = makeGeneralParameters(root);
        GeometryDescriptor geometryDescriptor = makeGeometryDescriptor(root);
        LayerManager layerManager = makeLayerManager(root, geometryDescriptor);
        Integrator integrator = makeIntegrator(root, p, layerManager);
        Runner runner = new Runner(p, integrator);
        return runner;
    }

    private static LayerManager makeLayerManager(Element root, GeometryDescriptor geometryDescriptor) {
        Element layerRoot = root.element("layers");
        LayerManager layerManager = LayerManagerFactory.instantiate(layerRoot, geometryDescriptor);
        return layerManager;
    }

    private static Integrator makeIntegrator(Element root, GeneralParameters p, LayerManager lm) {
        return IntegratorFactory.instantiate(root, p, lm);
    }

    private static GeneralParameters makeGeneralParameters(Element root) {
        Element gpRoot = root.element("general");
        GeneralParameters p = new GeneralParameters(gpRoot);
        return p;
    }

    private static GeometryDescriptor makeGeometryDescriptor(Element root) {
        Element geometryElem = root.element("geometry");
        GeometryDescriptor geometryDescriptor = GeometryDescriptorFactory.instantiate(geometryElem);
        return geometryDescriptor;
    }

    private static void validate(Element root) {
        Element ve = root.element("version");

        if (ve == null) {
            System.out.println("The specified project file does not contain an eSLIME version number.");
        }
        String version = ve.getText();

        if (!version.equalsIgnoreCase(VERSION)) {

            String msg = "Version mismatch. Parameter file written for eSLIME "
                    + version + ", but this is " + VERSION + ".";
            throw new IllegalArgumentException(msg);

        }
    }

    public static String getVersion() {
        return VERSION;
    }
}
