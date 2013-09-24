package io.parameters;

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
 * Note that 
 * @author dbborens
 *
 */
public class ProjectLoader {

	private Element root;
	
	public ProjectLoader(File toLoad) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(toLoad);
		validate(document);
		root = document.getRootElement();
	}
	
	private void validate(Document document) {
		// TODO Auto-generated method stub
		
	}

	public ProjectLoader(String toLoad) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(toLoad);
		validate(document);
		root = document.getRootElement();
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
}
