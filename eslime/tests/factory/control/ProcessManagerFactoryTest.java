/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control;//import junit.framework.TestCase;

import control.GeneralParameters;
import control.ProcessManager;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.EcoProcess;
import processes.MockProcess;
import test.EslimeLatticeTestCase;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerFactoryTest extends EslimeLatticeTestCase {

    private GeneralParameters p;
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();

        root = readXmlFile("factories/control/ProcessManagerFactoryTest.xml");
    }

    public void testImplicit() throws Exception {
        Element implicitRoot = root.element("implicit-case");

        ProcessManager actual = ProcessManagerFactory.instantiate(implicitRoot, p, layerManager);

        List<EcoProcess> processes = new ArrayList<>(0);
        ProcessManager expected = new ProcessManager(processes, layerManager);

        assertEquals(expected, actual);
    }

    public void testExplicit() throws Exception {
        Element explicitRoot = root.element("explicit-case");

        ProcessManager actual = ProcessManagerFactory.instantiate(explicitRoot, p, layerManager);

        List<EcoProcess> processes = new ArrayList<>(2);
        processes.add(mockProcess("test1"));
        processes.add(mockProcess("test2"));
        ProcessManager expected = new ProcessManager(processes, layerManager);

        assertEquals(expected, actual);
    }

    private EcoProcess mockProcess(String identifier) {
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);

        return new MockProcess(arguments, identifier, 1.0, 1);
    }
}