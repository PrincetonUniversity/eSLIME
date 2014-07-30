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

package io.visual.color;

import cells.Cell;
import cells.MockCell;
import control.arguments.ConstantDouble;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import io.visual.HSLColor;
import layers.MockLayerManager;
import layers.MockSystemState;
import layers.cell.CellLayer;
import test.EslimeTestCase;

import java.awt.*;

public class SurfaceGrowthColorManagerTest extends EslimeTestCase {

    private MockSystemState systemState;
    private SurfaceGrowthColorManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 5);
        Boundary boundary = new Absorbing(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);

        CellLayer layer = new CellLayer(geom);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        put(layer, 1, 1);
        put(layer, 2, 1);
        put(layer, 3, 1);

        systemState = new MockSystemState();
        systemState.setLayerManager(layerManager);

        DefaultColorManager base = new DefaultColorManager();
        query = new SurfaceGrowthColorManager(base, new ConstantDouble(0.5), new ConstantDouble(1.0));
    }

    public void testGetBorderColor() throws Exception {
        Color actual = query.getBorderColor();
        Color expected = Color.DARK_GRAY;
        assertEquals(expected, actual);
    }

    public void testInteriorPoint() throws Exception {
        Color actual = getColor(2);
        Color expected = scaleColor(Color.BLUE);

        assertEquals(expected, actual);
    }

    public void testExteriorPoint() throws Exception {
        Color actual = getColor(1);
        Color expected = Color.BLUE;

        assertEquals(expected, actual);
    }

    public void testVacantPoint() throws Exception {
        Color actual = getColor(0);
        Color expected = Color.BLACK;

        assertEquals(expected, actual);
    }

    private Color getColor(int pos) {
        Coordinate c = new Coordinate(0, pos, 0);
        Color ret = query.getColor(c, systemState);

        return ret;
    }

    private Color scaleColor(Color original) {
        float[] colorArr = HSLColor.fromRGB(original);
        float h = colorArr[0];
        float s = colorArr[1];
        float l = colorArr[2];
        Color ret = HSLColor.toRGB(h, s, l * 0.5F);
        return ret;
    }
    private void put(CellLayer layer, int pos, int state) {
        Coordinate coord = new Coordinate(0, pos, 0);
        Cell cell = new MockCell(state);
        layer.getUpdateManager().place(cell, coord);
    }
}