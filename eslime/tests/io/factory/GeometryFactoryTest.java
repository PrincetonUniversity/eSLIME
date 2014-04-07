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

package io.factory;

import geometry.Geometry;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.identifiers.Coordinate;
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
