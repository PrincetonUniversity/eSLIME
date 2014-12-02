/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.run;//import junit.framework.TestCase;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import test.EslimeTestCase;

public class DocumentFactoryTest extends EslimeTestCase {

    public void testInstantiate() throws Exception {
        Element expected = new BaseElement("fixture");

        String path = fixturePath + "factories/control/run/DocumentFactoryTest.xml";
        Element actual = DocumentFactory.instantiate(path);

        // TODO change this to a direct comparison after creating EcoElement objects
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.elements().size(), actual.elements().size());
    }
}