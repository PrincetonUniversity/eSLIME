/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes;

import control.GeneralParameters;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.EcoProcess;
import processes.MockProcess;
import test.EslimeLatticeTestCase;

import java.util.ArrayList;
import java.util.List;

public class ProcessListFactoryTest extends EslimeLatticeTestCase {

    private GeneralParameters p;
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
        root = readXmlFile("factories/processes/ProcessListFactoryTest.xml");
    }

    public void testImplicit() throws Exception {
        Element implicit = root.element("implicit-case");
        List<EcoProcess> expected = new ArrayList<>(0);
        List<EcoProcess> actual = ProcessListFactory.instantiate(implicit, layerManager, p);

        doComparison(expected, actual);
    }

    public void testSingleton() throws Exception {
        Element singleton = root.element("singleton-case");
        List<EcoProcess> expected = new ArrayList<>(1);
        expected.add(makeProcess("test"));
        List<EcoProcess> actual = ProcessListFactory.instantiate(singleton, layerManager, p);

        doComparison(expected, actual);
    }

    public void testMultiple() throws Exception {
        Element multiple = root.element("multiple-case");
        List<EcoProcess> expected = new ArrayList<>(2);
        expected.add(makeProcess("test1"));
        expected.add(makeProcess("test2"));
        List<EcoProcess> actual = ProcessListFactory.instantiate(multiple, layerManager, p);

        doComparison(expected, actual);
    }

    private void doComparison(List<EcoProcess> expected, List<EcoProcess> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private EcoProcess makeProcess(String identifier) {
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);
        return new MockProcess(arguments, identifier, 1.0, 1);
    }
}