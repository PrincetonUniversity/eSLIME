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

package io.loader;

import continuum.operations.Operator;
import geometry.Geometry;
import io.factory.OperatorFactory;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * @author dbborens
 * @test OperatorLoaderTest
 */
public class OperatorLoader {

    private OperatorFactory factory;

    public OperatorLoader(Geometry geometry, boolean useBoundaries) {
        factory = new OperatorFactory(geometry, useBoundaries);
    }

    public Operator[] getOperators(Element root) {
        ArrayList<Operator> operatorList = new ArrayList<Operator>();

        for (Object o : root.elements()) {
            Element e = (Element) o;
            Operator instance = factory.instantiate(e);
            operatorList.add(instance);
        }

        Operator[] operators = operatorList.toArray(new Operator[0]);

        return operators;
    }


}

