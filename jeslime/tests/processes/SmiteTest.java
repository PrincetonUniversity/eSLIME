package processes;

import cells.SimpleCell;
import geometry.MockGeometry;
import layers.cell.CellLayer;
import processes.cellular.Smite;
import processes.gillespie.GillespieState;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 12/24/13.
 */
public class SmiteTest extends EslimeTestCase {

    private Smite smite;
    private Coordinate query;

    // We can use a real cell layer, because they manage
    // their own child objects (update manager, viewer, indices)
    // and therefore are compatible with mock infrastructure.
    private CellLayer layer;

    @Override
    protected void setUp() throws Exception {
        // Construct coordinate to smite
        query = new Coordinate(0, 0, 0);
        Coordinate[] cc = new Coordinate[] {query};

        // Construct geometry
        MockGeometry geom = new MockGeometry();
        geom.setCanonicalSites(cc);

        // Construct process
        smite = new Smite(layer, geom, false);
        smite.setActiveSites(cc);

        // Construct layer
        layer = new CellLayer(geom, 0);

        // Construct process
        smite = new Smite(layer, geom, false);
    }

    /**
     * Make sure that the "target" method works as expected without
     * a Gillespie parent.
     *
     * @throws Exception
     */
    public void testTargetSimple() throws Exception {
        // Target is a pass-through without Gillespie, but it
        // should still work (i.e., not throw an exception.).
        smite.target(null);
    }

    /**
     * Make sure that the "target" method works as expected when
     * using Gillespie mode.
     */
    public void testTargetGillespie() throws Exception {
        Integer[] keys = new Integer[]{0};
        GillespieState state = new GillespieState(keys);

        // Gillespie state should be updated by target
        smite.target(state);
        state.close();
        assertEquals(1, state.getTotalCount());
        assertEquals(1.0, state.getTotalWeight(), epsilon);
    }

     /**
      * Makes sure that the process behaves as expected.
      *
      * @throws Exception
      */
    public void testLifeCycle() throws Exception {
        // Create a cell on the lattice
        SimpleCell cell = new SimpleCell(1);
        layer.getUpdateManager().place(cell, query);

        // Verify that the cell is there
        assertTrue(layer.getViewer().isOccupied(query));

        // Smite it
        smite.fire(null);

        // Verify that the cell is not there
        assertFalse(layer.getViewer().isOccupied(query));
    }
}
