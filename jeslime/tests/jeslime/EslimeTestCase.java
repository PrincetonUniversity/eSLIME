package jeslime;

import java.util.ArrayList;
import java.util.Arrays;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import junit.framework.TestCase;
import structural.Flags;
import structural.identifiers.Coordinate;

public abstract class EslimeTestCase extends TestCase {
	
	protected double epsilon = calcEpsilon();
	
	protected void assertArraysEqual(Coordinate[] expected, Coordinate[] actual, boolean sort) {
		assertEquals(expected.length, actual.length);
		
		if (sort) {
			Arrays.sort(actual);
			Arrays.sort(expected);
		}
		
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

	protected void assertArraysEqual(int[] actual, int[] expected, boolean sort) {
		assertEquals(expected.length, actual.length);
		
		if (sort) {
			Arrays.sort(actual);
			Arrays.sort(expected);
		}
		
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	
	protected Coordinate[] clean(Coordinate[] check) {
		ArrayList<Coordinate> retain = new ArrayList<Coordinate>(check.length);
		for (Coordinate c : check) {
			if (c != null) {
				retain.add(c);
			}
		}
		
		return retain.toArray(new Coordinate[0]);
	}
	
	private double calcEpsilon() {
		double eps = 1.0D;
		
		while ((1.0 + (eps / 2.0)) != 1.0) {
			eps /= 2.0;
		}
		
		return eps;
	}
	
	protected void addElement(Element e, String name, String text) {
		Element rElem = new BaseElement(name);
		rElem.setText(text);
		e.add(rElem);		
	}
}
