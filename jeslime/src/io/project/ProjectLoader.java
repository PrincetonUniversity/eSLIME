package io.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

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
	private final static String VERSION = "v0.3.1";
	
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

	public ProjectLoader(String toLoad) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(toLoad);
		validate(document);
		root = document.getRootElement();
		
		stringForm = document.asXML();
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
