/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.arguments.UniformDouble;
import factory.control.arguments.DoubleArgumentFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class DoubleArgumentFactoryTest extends EslimeTestCase {
    private Element root;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/DoubleArgumentFactoryTest.xml");
        random = new Random(RANDOM_SEED);
    }

    public void testNullNoDefault() {
        Element element = root.element("null-case");

        boolean thrown = false;

        try {
            DoubleArgumentFactory.instantiate(element, "not-there", random);
        } catch (Exception ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    public void testNullWithDefault() {
        Element element = root.element("null-case");

        Argument<Double> actual = DoubleArgumentFactory.instantiate(element, "not-there", 5.0, random);
        Argument<Double> expected = new ConstantDouble(5.0);

        assertEquals(expected, actual);
    }

    public void testConstantImplicit() {
        Element element = root.element("constant-implicit-case");

        Argument<Double> actual = DoubleArgumentFactory.instantiate(element, "test", 6.0, random);
        Argument<Double> expected = new ConstantDouble(5.0);

        assertEquals(expected, actual);
    }

    public void testConstantExplicit() {
        Argument<Double> actual = DoubleArgumentFactory.instantiate(root, "constant-explicit-case", 6.0, random);
        Argument<Double> expected = new ConstantDouble(5.0);

        assertEquals(expected, actual);
    }

    public void testUniform() {
        Argument<Double> actual = DoubleArgumentFactory.instantiate(root, "uniform-case", 6.0, random);
        Argument<Double> expected = new UniformDouble(1.7, 2.4, random);

        assertEquals(expected, actual);
    }

    public void testRecursive() {
        Argument<Double> actual = DoubleArgumentFactory.instantiate(root, "recursive-case", 6.0, random);
        Argument<Double> expected = new UniformDouble(-1.0, 2.0, random);

        assertEquals(expected, actual);
    }
}
