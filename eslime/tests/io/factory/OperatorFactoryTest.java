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

import continuum.operations.*;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import factory.continuum.operations.OperatorFactory;
import geometry.CubicMockGeometry;
import geometry.LinearMockGeometry;
import no.uib.cipr.matrix.Matrix;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.utilities.CoordinateSerializer;
import test.EslimeTestCase;

public class OperatorFactoryTest extends EslimeTestCase {

    private OperatorFactory factory2d;
    private OperatorFactory factory3d;

    private LinearMockGeometry geom2d;
    private CubicMockGeometry geom3d;

    private Coordinate origin2;
    private Coordinate origin3;

    public void setUp() {
        // Set up the geometry (including some cloodge for mocks)
        origin2 = new Coordinate(0, 0, 0);
        origin3 = new Coordinate(0, 0, 0, 0);

        geom2d = new LinearMockGeometry();
        geom3d = new CubicMockGeometry();

        geom2d.setWidth(1);
        geom3d.setWidth(1);

        // 2D -- x, y coord
        Coordinate[] sites2d = new Coordinate[]{
                origin2
        };

        // 3D -- x, y, z coord
        Coordinate[] sites3d = new Coordinate[]{
                origin3
        };

        // The following gets the mock geometry to a legal state
        geom2d.setCanonicalSites(sites2d);
        geom3d.setCanonicalSites(sites3d);

        geom2d.setCellNeighbors(origin2, new Coordinate[0]);
        geom3d.setCellNeighbors(origin3, new Coordinate[0]);

        factory2d = new OperatorFactory(geom2d, true);
        factory3d = new OperatorFactory(geom3d, false);
    }

    public void testInstantiateDiffusion() {
        // Initialize test objects
        Element e = new BaseElement("diffusion");
        addElement(e, "r", "0.1");

        Operator o2d = factory2d.instantiate(e);
        Operator o3d = factory3d.instantiate(e);

        // Make sure the matrix was initialized
        assertTrue(matrixNotEmpty(o2d));
        assertTrue(matrixNotEmpty(o3d));

        // Test class is correct
        assertEquals(Diffusion.class, o2d.getClass());
        assertEquals(Diffusion.class, o3d.getClass());

        // Test properties
        Diffusion d2d = (Diffusion) o2d;
        Diffusion d3d = (Diffusion) o3d;

        assertTrue(d2d.isUseBoundaries());
        assertFalse(d3d.isUseBoundaries());

        assertEquals(0.1, d2d.getD(), epsilon);
        assertEquals(0.1, d3d.getD(), epsilon);

    }

    public void testInstantiateAdvection() {
        // Initialize test objects
        Coordinate disp2 = new Coordinate(1, 0, Flags.VECTOR);
        Coordinate disp3 = new Coordinate(1, 0, 0, Flags.VECTOR);

        Element e2 = new BaseElement("advection");
        addElement(e2, "r", "0.1");

        Element e3 = new BaseElement("advection");
        addElement(e3, "r", "0.1");

        Element de2 = CoordinateSerializer.serialize(disp2, "displacement");
        Element de3 = CoordinateSerializer.serialize(disp3, "displacement");

        e2.add(de2);
        e3.add(de3);

        Operator o2d = factory2d.instantiate(e2);
        Operator o3d = factory3d.instantiate(e3);

        // Make sure the matrix was initialized
        assertTrue(matrixNotEmpty(o2d));
        assertTrue(matrixNotEmpty(o3d));

        // Test class is correct
        assertEquals(Advection.class, o2d.getClass());
        assertEquals(Advection.class, o3d.getClass());

        // Test properties
        Advection d2d = (Advection) o2d;
        Advection d3d = (Advection) o3d;

        assertTrue(d2d.isUseBoundaries());
        assertFalse(d3d.isUseBoundaries());

        assertEquals(0.1, d2d.getR(), epsilon);
        assertEquals(0.1, d3d.getR(), epsilon);

        assertEquals(disp2, d2d.getDisplacement());
        assertEquals(disp3, d3d.getDisplacement());

    }

    public void testInstantiateScaling() {
        // Initialize test objects
        Element e = new BaseElement("scaling");
        addElement(e, "lambda", "0.1");

        Operator o2d = factory2d.instantiate(e);
        Operator o3d = factory3d.instantiate(e);

        // Make sure the matrix was initialized
        assertTrue(matrixNotEmpty(o2d));
        assertTrue(matrixNotEmpty(o3d));

        // Test class is correct
        assertEquals(Scaling.class, o2d.getClass());
        assertEquals(Scaling.class, o3d.getClass());

        // Test properties
        Scaling d2d = (Scaling) o2d;
        Scaling d3d = (Scaling) o3d;

        assertTrue(d2d.isUseBoundaries());
        assertFalse(d3d.isUseBoundaries());

        assertEquals(0.1, d2d.getLambda(), epsilon);
        assertEquals(0.1, d3d.getLambda(), epsilon);

    }

    public void testInstantiateCompound() {
        // Build compound process element
        Element e = new BaseElement("compound");
        Element ce = new BaseElement("children");
        Element cc1 = new BaseElement("scaling");
        Element cc2 = new BaseElement("scaling");
        addElement(cc1, "lambda", "0.1");
        addElement(cc2, "lambda", "0.2");
        ce.add(cc1);
        ce.add(cc2);
        e.add(ce);

        // Initialize operators
        Operator o2d = factory2d.instantiate(e);
        Operator o3d = factory3d.instantiate(e);

        // Make sure the matrix was initialized
        assertTrue(matrixNotEmpty(o2d));
        assertTrue(matrixNotEmpty(o3d));

        // Test class is correct
        assertEquals(CompoundOperator.class, o2d.getClass());
        assertEquals(CompoundOperator.class, o3d.getClass());

        // Test properties
        CompoundOperator d2d = (CompoundOperator) o2d;
        CompoundOperator d3d = (CompoundOperator) o3d;

        assertTrue(d2d.isUseBoundaries());
        assertFalse(d3d.isUseBoundaries());
    }

    public void testStopIfUndefined() {
        Element e = new BaseElement("not-a-real-thing");

        boolean thrown = false;
        try {
            factory2d.instantiate(e);
        } catch (Exception exception) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try {
            factory3d.instantiate(e);
        } catch (Exception exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    private boolean matrixNotEmpty(Matrix m) {
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numColumns(); j++) {
                if (m.get(i, j) > epsilon) {
                    return true;
                }
            }
        }

        return false;
    }
}
