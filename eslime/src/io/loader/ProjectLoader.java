/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.loader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Takes in either a file or a string. Interprets the contents as
 * an XML document. Verifies that general structure of the document
 * is that of an eSLIME project file. Provides handles to
 * sub-elements.
 * <p/>
 * It is important, both for speed and resource management, to load
 * elements into native data structures and close the ProjectLoader
 * early on.
 *
 * @author dbborens
 * @untested
 */
public class ProjectLoader {

    // Version -- checked against parameters file to make sure they're
    // compatible
    private final static String VERSION = "0.5.6";

    private Element root;
    private String stringForm;

    public ProjectLoader(File toLoad) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(toLoad);
        validate(document);
        root = document.getRootElement();
        stringForm = document.asXML();
    }


    private void validate(Document document) {
        Element ve = document.getRootElement().element("version");

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

    public Element getElement(String element) {
        return root.element(element);
    }

    public boolean isAvailable() {
        return (root != null);
    }

    public void close() {
        root = null;
    }

    public String toString() {
        return stringForm;
    }


    public String getVersion() {
        return VERSION;
    }
}
