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

package test;

import geometry.MockGeometry;
import junit.framework.TestCase;
import junitx.framework.FileAssert;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.BaseElement;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class EslimeTestCase extends TestCase {

    protected final String eslimeRoot = "./eslime/";
    protected final String outputPath = eslimeRoot + "/output/";
    protected final String fixturePath = eslimeRoot + "/fixtures/";
    protected final int RANDOM_SEED = 0;
    protected double epsilon = calcEpsilon();

    protected static void assertNotEquals(Object p, Object q) {
        boolean equality = p.equals(q);
        assertFalse(equality);
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

    protected void assertArraysEqual(double[] actual, double[] expected, boolean sort) {
        assertEquals(expected.length, actual.length);

        if (sort) {
            Arrays.sort(actual);
            Arrays.sort(expected);
        }

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], epsilon);
        }
    }

    protected void assertArraysEqual(boolean[] actual, boolean[] expected) {
        assertEquals(expected.length, actual.length);

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
        Coordinate[] canonicals = new Coordinate[]{
                new Coordinate(0, 0, 0, 0),
                new Coordinate(0, 0, 1, 0),
                new Coordinate(0, 1, 0, 0),
                new Coordinate(0, 1, 1, 0),
                new Coordinate(1, 0, 0, 0)
        };
        MockGeometry ret = new MockGeometry();
        ret.setCanonicalSites(canonicals);

        return ret;
    }

    protected void assertFilesEqual(String filename) {
        String fixture = fixturePath + filename;
        String output = outputPath + filename;

        File fixtureFile = new File(fixture);
        File outputFile = new File(output);

        FileAssert.assertEquals(fixtureFile, outputFile);
    }

    protected void assertBinaryFilesEqual(String filename) {
        String fixture = fixturePath + filename;
        String output = outputPath + filename;

        File fixtureFile = new File(fixture);
        File outputFile = new File(output);

        FileAssert.assertBinaryEquals(fixtureFile, outputFile);
    }

    protected void assertBinaryFilesEqual(String fixtureName, String outputName) {
        String fixture = fixturePath + fixtureName;
        String output = outputPath + outputName;

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

    protected MockGeneralParameters makeMockGeneralParameters() {
        MockGeneralParameters ret = new MockGeneralParameters();
        ret.setInstancePath(outputPath);
        ret.setPath(outputPath);
        ret.initializeRandom(RANDOM_SEED);
        return ret;
    }
}
