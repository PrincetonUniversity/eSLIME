package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import geometry.MockGeometry;
import junitx.framework.FileAssert;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.BaseElement;

import junit.framework.TestCase;
import structural.identifiers.Coordinate;

public abstract class EslimeTestCase extends TestCase {
	
	protected double epsilon = calcEpsilon();

    protected final String eslimeRoot = "./jeslime/";
    protected final String outputPath = eslimeRoot + "/output/";
    protected final String fixturePath = eslimeRoot + "/fixtures/";

    // Superceded by Comparable[] implementation
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

    // Superceded by Comparable[] implementation
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

    protected void assertArraysEqual(Comparable[] expected, Comparable[] actual, boolean sort) {
        assertEquals(expected.length, actual.length);

        if (sort) {
            Arrays.sort(actual);
            Arrays.sort(expected);
        }

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

    }

    protected void assertArraysNotEqual(Comparable[] expected, Comparable[] actual, boolean sort) {
        // If the arrays are of unequal length, we have satisfied the assertion.
        if (expected.length != actual.length)
            return;

        if (sort) {
            Arrays.sort(actual);
            Arrays.sort(expected);
        }

        // If any element of the array is unequal, we have satisfied the assertion.
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                return;
            }
            assertEquals(expected[i], actual[i]);
        }

        // In all other cases, the assertion has failed.
        fail("Arrays are equal when they were expected to be unequal.");
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

    /**
     * Generate a basic mock geometry with a defined
     * set of canonical coordinates.
     */
    protected MockGeometry buildMockGeometry() {
        Coordinate[] canonicals = new Coordinate[] {
            new Coordinate(0, 0, 0, 0),
            new Coordinate(0, 0, 1, 0),
            new Coordinate(0, 1, 0, 0),
            new Coordinate(0, 1, 1, 0),
            new Coordinate(1, 0, 0, 0)
        };  MockGeometry ret = new MockGeometry();
        ret.setCanonicalSites(canonicals);

        return ret;
    }

    protected void assertFilesEqual(String filename) {
        String fixture = fixturePath + filename;
        String output  = outputPath + filename;

        File fixtureFile = new File(fixture);
        File outputFile = new File(output);

        FileAssert.assertEquals(fixtureFile, outputFile);
    }

    protected void assertBinaryFilesEqual(String filename) {
        String fixture = fixturePath + filename;
        String output  = outputPath + filename;

        File fixtureFile = new File(fixture);
        File outputFile = new File(output);

        FileAssert.assertBinaryEquals(fixtureFile, outputFile);
    }

    protected Element readXmlFile(String fileName) throws IOException, DocumentException {

        String path = fixturePath + fileName;
        File file = new File(path);
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        return root;
    }
}