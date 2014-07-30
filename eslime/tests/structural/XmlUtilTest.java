/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import org.dom4j.Element;
import structural.utilities.XmlUtil;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/20/14.
 */
public class XmlUtilTest extends EslimeTestCase {
    Element fixtureRoot;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("XmlUtilTest.xml");
    }

    public void testGetBoolean() throws Exception {
        Element testRoot = fixtureRoot.element("get-boolean");
        doBooleanTest(testRoot, "flag-only", true);
        doBooleanTest(testRoot, "no-flag", false);
        doBooleanTest(testRoot, "tag-true", true);
        doBooleanTest(testRoot, "tag-false", false);
    }

    private void doBooleanTest(Element testRoot, String childName, boolean expected) {
        Element child = testRoot.element(childName);
        boolean actual = XmlUtil.getBoolean(child, "test");
        assertEquals(expected, actual);
    }

    private void doIntegerTest(Element testRoot, String childName, int expected) {
        Element child = testRoot.element(childName);
        int actual = XmlUtil.getInteger(child, "test", -1);
        assertEquals(expected, actual);
    }

    private void doDoubleTest(Element testRoot, String childName, double expected) {
        Element child = testRoot.element(childName);
        double actual = XmlUtil.getDouble(child, "test", -1.0);
        assertEquals(expected, actual, epsilon);
    }

    public void testGetDouble() throws Exception {
        Element testRoot = fixtureRoot.element("get-double");
        doDoubleTest(testRoot, "default", -1.0);
        doDoubleTest(testRoot, "negative", -0.5);
        doDoubleTest(testRoot, "scientific", 4e-3);
    }

    public void testGetInteger() throws Exception {
        Element testRoot = fixtureRoot.element("get-integer");
        doIntegerTest(testRoot, "default", -1);
        doIntegerTest(testRoot, "negative", -5);
        doIntegerTest(testRoot, "positive", 2);
    }

    public void testGetStringDefault() throws Exception {
        Element testRoot = fixtureRoot.element("get-string");
        doStringTest(testRoot, "default", "default value", "default value");
        doStringTest(testRoot, "empty", "default value", "");
        doStringTest(testRoot, "non-empty", "default value", "test");
    }

    public void testGetStringNoDefault() throws Exception {
        Element testRoot = fixtureRoot.element("get-string");
        doStringTest(testRoot, "empty", "");
        doStringTest(testRoot, "non-empty", "test");

        boolean thrown = false;
        try {
            XmlUtil.getString(testRoot, "default");
        } catch (Exception e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    public void testGetEmptyIntArray() throws Exception {
        Element testRoot = fixtureRoot.element("get-int-array");
        int[] actual = XmlUtil.getIntegerArray(testRoot.element("empty"), "item");
        int[] expected = new int[0];
        assertArraysEqual(expected, actual, false);
    }

    public void testGetIntArray() throws Exception {
        Element testRoot = fixtureRoot.element("get-int-array");
        int[] actual = XmlUtil.getIntegerArray(testRoot.element("non-empty"), "item");
        int[] expected = new int[] {3, 4, -1};
        assertArraysEqual(expected, actual, false);
    }

    private void doStringTest(Element testRoot, String elemName, String defaultValue, String expected) {
        String actual = XmlUtil.getString(testRoot, elemName, defaultValue);
        assertEquals(expected, actual);
    }

    private void doStringTest(Element testRoot, String elemName, String expected) {
        String actual = XmlUtil.getString(testRoot, elemName);
        assertEquals(expected, actual);
    }
}
