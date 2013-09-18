package control;

import java.io.File;
import java.util.Iterator;

import org.dom4j.*;
import org.dom4j.io.*;

import structural.identifiers.Coordinate;

public class Sandbox {

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		SAXReader reader = new SAXReader();
		File file = new File("/Users/dbborens/github/jeSLIME/jeslime/projects/sandbox.xml");
		Document document = reader.read(file);
	
		Element root = document.getRootElement();
		
		/*
		// Make sure I know how to get all items
		for (Object o : general.elements()) {
			Element e = (Element) o;
			System.out.println(e.getName() + "=" + e.getData());
		}
		
		
		// Now make sure I know how to get particular items
		Element rSeed = general.element("random-seed");
		System.out.println("   The random seed is " + rSeed.getData());
		
		*/
		
		Element general = root.element("general");

		Element fElem = general.element("output-frames");
		
		for (Object o : fElem.elements("frame")) {
			Element e = (Element) o;
			System.out.println(e.getData());
		}
		
	}
}
