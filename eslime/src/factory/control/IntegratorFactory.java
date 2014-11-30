/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control;

import control.GeneralParameters;
import control.Integrator;
import control.ProcessManager;
import factory.io.serialize.SerializationFactory;
import io.serialize.SerializationManager;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Created by dbborens on 11/26/14.
 */
public abstract class IntegratorFactory {

    public static Integrator instantiate(Element root, GeneralParameters p, LayerManager lm) {
        ProcessManager processManager = makeProcessManager(root, p, lm);
        SerializationManager serializationManager = makeSerializationManager(root, p, lm);
        Integrator integrator = new Integrator(p, processManager, serializationManager);
        return integrator;
    }


    private static SerializationManager makeSerializationManager(Element root, GeneralParameters p, LayerManager lm) {
        Element writers = root.element("writers");
        SerializationManager mgr = SerializationFactory.makeManager(writers, lm, p);
        return mgr;
    }

    private static ProcessManager makeProcessManager(Element root, GeneralParameters p, LayerManager lm) {
        ProcessManager processManager = ProcessManagerFactory.instantiate(root, p, lm);
        return processManager;
    }

}
