/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package io.project;

import java.io.File;

import org.dom4j.*;
import org.dom4j.io.*;

/**
 * Takes in either a file or a string. Interprets the contents as
 * an XML document. Verifies that general structure of the document
 * is that of a jeSLIME parameters file. Provides handles to 
 * sub-elements.
 * 
 * It is important, both for speed and resource management, to load
 * elements into native data structures and close the ParameterLoader
 * early on.
 * 
 * @untested
 * @author dbborens
 *
 */
public class ProjectLoader {
	
	// Version -- checked against parameters file to make sure they're
	// compatible
	private final static String VERSION = "0.5.0";
	
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

	/*public ProjectLoader(String toLoad) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(toLoad);
		validate(document);
		root = document.getRootElement();

		stringForm = document.asXML();
	}*/

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
