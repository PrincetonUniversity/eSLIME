/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.loader;

import continuum.operations.Operator;
import geometry.Geometry;
import factory.continuum.operations.OperatorFactory;
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

