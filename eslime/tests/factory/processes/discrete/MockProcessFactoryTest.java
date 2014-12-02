/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes.discrete;

import control.GeneralParameters;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.MockProcess;
import test.EslimeLatticeTestCase;

public class MockProcessFactoryTest extends EslimeLatticeTestCase {

    private GeneralParameters p;
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/processes/discrete/MockProcessFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    public void testDefault() throws Exception {
        Element testElem = root.element("implicit-case");
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);

        MockProcess expected = new MockProcess(arguments, "", 1.0, 1);
        MockProcess actual = MockProcessFactory.instantiate(testElem, layerManager, p, 0);

        assertEquals(expected, actual);
    }

    public void testExplicit() throws Exception {
        Element testElem = root.element("explicit-case");
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);

        MockProcess expected = new MockProcess(arguments, "test", 4.0, 2);
        MockProcess actual = MockProcessFactory.instantiate(testElem, layerManager, p, 0);

        assertEquals(expected, actual);
    }
}