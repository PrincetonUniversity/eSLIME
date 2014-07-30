/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import test.EslimeTestCase;

import java.io.File;
import java.util.List;

/**
 * Created by David B Borenstein on 12/22/13.
 */
public class XmlTest extends EslimeTestCase {

    public void testOrder() throws DocumentException {

        // Open the fixture.
        String filename = fixturePath + "XmlTest.xml";
        File testFile = new File(filename);
        SAXReader reader = new SAXReader();
        Document document = reader.read(testFile);
        Element root = document.getRootElement();

        // There should be three elements within the root.
        List<Element> middles = root.elements();
        assertEquals(3, middles.size());

        Element query;
        // The first element should have four children.
        query = middles.get(0);
        assertEquals(4, query.elements().size());

        int i = 0;
        String[] expected = new String[]{"a", "b", "c", "d"};

        for (Object inner : query.elements()) {
            Element e = (Element) inner;

            // Verify the data tags (in order) of the child elements
            Element dataElem = e.element("data");
            assertNotNull(dataElem);

            String data = dataElem.getText();

            assertEquals(expected[i], data);
            i++;
        }

        // The second element should also have two children.
        query = middles.get(1);
        assertEquals(2, query.elements().size());

        // The third element should have three children, of different classes.
        query = middles.get(2);
        assertEquals(3, query.elements().size());

        String[] expectedNames = {"alpha", "beta", "alpha"};
        String[] expectedIDs = {"1", "3", "2"};
        i = 0;
        for (Object inner : query.elements()) {
            Element e = (Element) inner;

            // Verify the elements are loaded in specified order.
            assertEquals(expectedNames[i], e.getName());
            Attribute id = e.attribute("id");
            assertEquals(expectedIDs[i], id.getValue());

            i++;
        }
    }
}
