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

package io.factory;

import io.loader.ProcessLoader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import test.EslimeTestCase;

import java.io.File;

/**
 * Created by David B Borenstein on 12/22/13.
 */
public class ProcessLoaderTest extends EslimeTestCase {

    private ProcessLoader loader;

    @Override
    protected void setUp() throws Exception {
        String path = fixturePath + "CellProcessLoaderTest.xml";
        File file = new File(path);
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        loader = new ProcessLoader(root);
    }

    public void testGetProcess() throws Exception {
        assertIdentifiersEqual(0, "alpha");
        assertIdentifiersEqual(1, "beta");
        assertIdentifiersEqual(2, "gamma");
    }

    public void testGetProcesses() throws Exception {
        Comparable[] expected = new Integer[]{0, 1, 2};
        Comparable[] actual = loader.getProcesses();
        assertArraysEqual(expected, actual, true);
    }

    private void assertIdentifiersEqual(int id, String expected) {
        Element e = loader.getProcess(id);
        String actual = getIdentifier(e);
        assertEquals(expected, actual);
    }

    private String getIdentifier(Element e) {
        // First, make sure this is the right kind of element
        assertEquals("mock-process", e.getName());

        // Now, get the identifier
        Element idElem = e.element("identifier");
        assertNotNull(idElem);

        return idElem.getText();
    }
}
