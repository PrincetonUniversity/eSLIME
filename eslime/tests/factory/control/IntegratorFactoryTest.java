/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control;//import junit.framework.TestCase;

import control.GeneralParameters;
import control.Integrator;
import control.ProcessManager;
import io.serialize.SerializationManager;
import io.serialize.Serializer;
import io.serialize.interactive.ProgressReporter;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.EcoProcess;
import processes.MockProcess;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

import java.util.ArrayList;
import java.util.List;

public class IntegratorFactoryTest extends EslimeLatticeTestCase {

    private GeneralParameters p;
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = new MockGeneralParameters();
        root = readXmlFile("factories/control/IntegratorFactoryTest.xml");
    }

    public void testExplicitCase() throws Exception {
        Element explicitRoot = root.element("explicit-case");
        Integrator actual = IntegratorFactory.instantiate(explicitRoot, p, layerManager);

        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);
        EcoProcess process = new MockProcess(arguments, "test1", 1.0, 1);
        List<EcoProcess> processes = new ArrayList<>(1);
        processes.add(process);
        ProcessManager processManager = new ProcessManager(processes, layerManager);

        Serializer writer = new ProgressReporter(p, layerManager);
        List<Serializer> writers = new ArrayList<>(1);
        writers.add(writer);
        SerializationManager serializationManager = new SerializationManager(p, layerManager, writers);

        Integrator expected = new Integrator(p, processManager, serializationManager);

        assertEquals(expected, actual);
    }

    public void testImplicitCase() throws Exception {
        Element implicitRoot = root.element("implicit-case");
        Integrator actual = IntegratorFactory.instantiate(implicitRoot, p, layerManager);
        ProcessManager processManager = new ProcessManager(new ArrayList<EcoProcess>(0), layerManager);
        SerializationManager serializationManager = new SerializationManager(p, layerManager, new ArrayList<Serializer>(0));
        Integrator expected = new Integrator(p, processManager, serializationManager);

        assertEquals(expected, actual);
    }
}
