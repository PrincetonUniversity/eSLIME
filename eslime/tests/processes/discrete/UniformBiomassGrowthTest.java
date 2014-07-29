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

package processes.discrete;

import cells.BehaviorCell;
import cells.Cell;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingHard;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.set.CompleteSet;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;

public class UniformBiomassGrowthTest extends TestCase {

    private double epsilon = calcEpsilon();

    public void testUndeferred() {
        // Set up a dummy simulation
        //Geometry geom = new HexRing(6, 6);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 6, 6);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CellLayer layer = new CellLayer(geom);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        // Create two cells that can be fed
        Cell one = new BehaviorCell(layerManager, 1, 0.7, 2.0);
        Cell two = new BehaviorCell(layerManager, 2, 0.3, 2.0);

        Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
        Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);

        layer.getUpdateManager().place(one, coord1);
        layer.getUpdateManager().place(two, coord2);

        layerManager.setCellLayer(layer);

        // Create a UniformBiomassGrowth
        CellProcess process = new UniformBiomassGrowth(layerManager, new CompleteSet(geom), 1.5, false);

        // Verify that they get fed when the process is invoked
        assertEquals(layer.getViewer().getCell(coord1).getHealth(), 0.7, epsilon);
        assertEquals(layer.getViewer().getCell(coord2).getHealth(), 0.3, epsilon);

        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));

        try {
            process.iterate();
        } catch (Exception ex) {
            fail();
        }

        // Verify that they get fed when the process is invoked
        assertEquals(layer.getViewer().getCell(coord1).getHealth(), 2.2, epsilon);
        assertEquals(layer.getViewer().getCell(coord2).getHealth(), 1.8, epsilon);

        assertTrue(layer.getViewer().getDivisibleSites().contains(coord1));
        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
    }

//    public void testDeferred() {
//        // Set up a dummy simulation
//        //Geometry geom = new HexRing(6, 6);
//        Lattice lattice = new TriangularLattice();
//        Shape shape = new Rectangle(lattice, 6, 6);
//        Boundary boundary = new PlaneRingHard(shape, lattice);
//        Geometry geom = new Geometry(lattice, shape, boundary);
//        CellLayer layer = new CellLayer(geom);
//        MockLayerManager layerManager = new MockLayerManager();
//        layerManager.setCellLayer(layer);
//        // Create two cells that can be fed
//        Cell one = new BehaviorCell(layerManager, 1, 0.7, 2.0);
//        Cell two = new BehaviorCell(layerManager, 2, 0.3, 2.0);
//
//        Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
//        Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);
//
//        layer.getUpdateManager().place(one, coord1);
//        layer.getUpdateManager().place(two, coord2);
//
//        layerManager.setCellLayer(layer);
//
//        // Create a UniformBiomassGrowth
//        CellProcess process = new UniformBiomassGrowth(layerManager, 1.5, true);
//
//        // Verify that they get fed when the process is invoked
//        assertEquals(layer.getViewer().getCell(coord1).getHealth(), 0.7, epsilon);
//        assertEquals(layer.getViewer().getCell(coord2).getHealth(), 0.3, epsilon);
//
//        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
//        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
//
//        try {
//            process.iterate();
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//
//        assertEquals(layer.getViewer().getCell(coord1).getHealth(), 0.7, epsilon);
//        assertEquals(layer.getViewer().getCell(coord2).getHealth(), 0.3, epsilon);
//
//        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
//        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
//
//        layer.getUpdateManager().apply(coord1);
//        layer.getUpdateManager().apply(coord2);
//
//        // Verify that they get fed when the process is invoked
//        assertEquals(layer.getViewer().getCell(coord1).getHealth(), 2.2, epsilon);
//        assertEquals(layer.getViewer().getCell(coord2).getHealth(), 1.8, epsilon);
//
//        assertTrue(layer.getViewer().getDivisibleSites().contains(coord1));
//        assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
//    }

    private double calcEpsilon() {
        double machEps = 1.0d;

        do {
            machEps /= 2d;
        } while (1d + (machEps / 2d) != 1d);

        return machEps;
    }
}
