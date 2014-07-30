/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.factory;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.arguments.UniformInteger;
import factory.control.arguments.IntegerArgumentFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class IntegerArgumentFactoryTest extends EslimeTestCase {

    private Element root;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/IntegerArgumentFactoryTest.xml");
        random = new Random(RANDOM_SEED);
    }

    public void testNullNoDefault() {
        Element element = root.element("null-case");

        boolean thrown = false;

        try {
            IntegerArgumentFactory.instantiate(element, "not-there", random);
        } catch (Exception ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    public void testNullWithDefault() {
        Element element = root.element("null-case");

        Argument<Integer> actual = IntegerArgumentFactory.instantiate(element, "not-there", 5, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testConstantImplicit() {
        Element element = root.element("constant-implicit-case");

        Argument<Integer> actual = IntegerArgumentFactory.instantiate(element, "test", 6, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testConstantExplicit() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "constant-explicit-case", 6, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testUniform() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "uniform-case", 6, random);
        Argument<Integer> expected = new UniformInteger(1, 3, random);

        assertEquals(expected, actual);
    }

    public void testRecursive() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "recursive-case", 6, random);
        Argument<Integer> expected = new UniformInteger(-1, 2, random);

        assertEquals(expected, actual);
    }
}
