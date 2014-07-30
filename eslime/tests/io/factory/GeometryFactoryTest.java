/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import control.identifiers.Coordinate;
import factory.geometry.GeometryFactory;
import geometry.Geometry;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 12/30/13.
 */
public class GeometryFactoryTest extends EslimeTestCase {

    public void testLifeCycle() {
        // Initialize the GM.
        Element root = makeRoot();
        GeometryFactory gm = new GeometryFactory(root);

        // Make a geometry.
        Geometry prh = gm.make(boundary("PlaneRingHard"));

        // Make another geometry.
        Geometry arena = gm.make(boundary("Arena"));

        // Verify implicitly that the two geometries have the right shape and lattice.
        Coordinate interior = new Coordinate(2, 2, 0);

        assertArraysEqual(prh.getNeighbors(interior, Geometry.APPLY_BOUNDARIES), arena.getNeighbors(interior, Geometry.APPLY_BOUNDARIES), true);
        assertEquals(4, prh.getNeighbors(interior, Geometry.APPLY_BOUNDARIES).length);

        Coordinate corner = new Coordinate(0, 0, 0);
        assertArraysNotEqual(prh.getNeighbors(corner, Geometry.APPLY_BOUNDARIES), arena.getNeighbors(corner, Geometry.APPLY_BOUNDARIES), true);
    }

    private Element makeRoot() {
        Element root = new BaseElement("geometry");

        //Element boundary = new BaseElement("boundary");

        Element lattice = new BaseElement("lattice");
        addElement(lattice, "class", "rectangular");

        Element shape = new BaseElement("shape");
        addElement(shape, "class", "rectangle");
        addElement(shape, "height", "10");
        addElement(shape, "width", "5");
        root.add(lattice);
        root.add(shape);

        return root;
    }

    private Element boundary(String className) {
        Element boundary = new BaseElement("boundary");
        addElement(boundary, "class", className);

        Element root = new BaseElement("layer");
        root.add(boundary);
        return root;
    }
}
