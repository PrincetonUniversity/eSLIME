package agent.targets;

import cells.MockCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/10/14.
 */
public class TargetRuleTest extends EslimeTestCase {

    private MockGeometry geom;
    private MockLayerManager layerManager;
    private CellLayer cellLayer;
    private MockCell self, occupiedNeighbor;
    private Coordinate[] cc, neighbors;
    private Coordinate left, right, center;

    @Override
    public void setUp() {
        geom = new MockGeometry();

        center = new Coordinate(1, 0, 0);
        left = new Coordinate(0, 0, 0);
        right = new Coordinate(2, 0, 0);

        cc = new Coordinate[] { center, left, right };

        geom.setCanonicalSites(cc);

        layerManager = new MockLayerManager();
        cellLayer = new CellLayer(geom, 0);
        layerManager.setCellLayer(cellLayer);

        occupiedNeighbor = new MockCell();
        self = new MockCell();

        // Only one neighbor is occupied; the other is not.
        cellLayer.getUpdateManager().place(self, center);
        cellLayer.getUpdateManager().place(occupiedNeighbor, left);

        // Associate the neighborhood with the coordinate
        neighbors = new Coordinate[] {left, right};
        geom.setCellNeighbors(center, neighbors);
    }

    public void testTargetAllNeighbors() {
        System.out.println();
        TargetRule query = new TargetAllNeighbors(self, layerManager);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = neighbors;

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetVacantNeighbors() {
        TargetRule query = new TargetVacantNeighbors(self, layerManager);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {right};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetOccupiedNeighbors() {
        TargetRule query = new TargetOccupiedNeighbors(self, layerManager);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {left};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetSelf() {
        TargetRule query = new TargetSelf(self, layerManager);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {center};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetCaller() {
        // Left caller
        TargetRule query = new TargetCaller(self, layerManager);
        Coordinate[] actual = query.report(occupiedNeighbor);
        Coordinate[] expected = new Coordinate[] {left};
        assertArraysEqual(expected, actual, true);

    }

    // Null caller: should blow up
    public void testTargetCallerNull() {
        TargetRule query = new TargetCaller(self, layerManager);
        boolean thrown = false;
        try {
            query.report(null);
        } catch (IllegalStateException ex) {
            thrown = true;
        } catch (NullPointerException ex) {
            fail();
        }

        assertTrue(thrown);
    }

    public void testEquality() {
        // Equality is defined at the superclass level, so one test is sufficient.

        TargetRule p, q, r;

        // Make two targeters of the same class, but with different callbacks
        p = new TargetSelf(new MockCell(), layerManager);
        q = new TargetSelf(new MockCell(), layerManager);

        // Make one targeter of a different class
        r = new TargetCaller(new MockCell(), layerManager);

        // Test that the two of the same class are equal
        assertEquals(p, q);

        // Test that the two of different classes are not equal
        assertNotEquals(p, r);
    }

    public void testClone()  {
        MockCell parent = new MockCell();
        TargetRule[] rules = new TargetRule[] {
            new TargetAllNeighbors(parent, layerManager),
            new TargetCaller(parent, layerManager),
            new TargetOccupiedNeighbors(parent, layerManager),
            new TargetSelf(parent, layerManager),
            new TargetVacantNeighbors(parent, layerManager)
        };

        for (TargetRule rule : rules) {
            doCloneTest(rule, parent);
        }
    }

    private void doCloneTest(TargetRule original, MockCell parent) {
        MockCell child = new MockCell();
        TargetRule cloned = original.clone(child);
        assertEquals(original, cloned);
        assertEquals(parent, original.getCallback());
        assertEquals(child, cloned.getCallback());
    }
}
