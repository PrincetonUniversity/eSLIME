/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;

import control.GeneralParameters;
import control.arguments.CellDescriptor;
import control.arguments.ConstantDouble;
import control.arguments.ConstantInteger;
import org.dom4j.Element;
import test.EslimeLatticeTestCase;

public class CellDescriptorFactoryTest extends EslimeLatticeTestCase {

    private Element root;
    private GeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/control/arguments/CellDescriptorFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    public void testImplicit() throws Exception {
        Element implicitRoot = root.element("implicit-case");
        CellDescriptor actual = CellDescriptorFactory.instantiate(implicitRoot, layerManager, p);

        CellDescriptor expected = new CellDescriptor(layerManager);
        expected.setCellState(new ConstantInteger(1));
        expected.setInitialHealth(new ConstantDouble(0.5));
        expected.setThreshold(new ConstantDouble(1.0));

        assertEquals(expected, actual);
    }

//    public void testExplicit() throws Exception {
//        Element explicitRoot = root.element("explicit-case");
//        CellDescriptor actual = CellDescriptorFactory.instantiate(explicitRoot, layerManager, p);
//
//        CellDescriptor expected = new CellDescriptor(layerManager, p);
//        expected.setCellState(new ConstantInteger(7));
//        expected.setInitialHealth(new ConstantDouble(2.0));
//        expected.setThreshold(new ConstantDouble(3.0));
//
//        assertEquals(expected, actual);
//    }
}