package processes.discrete.filter;//import junit.framework.TestCase;

import cells.MockCell;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import test.EslimeTestCase;

import java.util.ArrayList;
import java.util.List;

public class DepthFilterTest extends EslimeTestCase {


    private Geometry geom;
    private MockLayerManager layerManager;
    private CellLayer layer;
    private List<Coordinate> initial;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geom = makeLinearGeometry(10);
        layer = new CellLayer(geom);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        placeCells();
    }

    // Position  0 1 2 3 4 5 6 7 8 9
    // Cell          2 3 4 5 6
    // Depth     0 0 0 1 2 1 0 0 0 0
    private void placeCells() throws Exception {
        initial = new ArrayList<>();
        for (int y = 2; y < 7; y++) {
            Coordinate c = new Coordinate(0, y, 0);
            MockCell cell = new MockCell(y);
            layerManager.getCellLayer().getUpdateManager().place(cell, c);
            initial.add(c);
        }
    }

    public void testSurfaceCase() {
        DepthFilter query = new DepthFilter(layer, new ConstantInteger(0));
        List<Coordinate> actual = query.apply(initial);

        List<Coordinate> expected = new ArrayList<>();
        expected.add(new Coordinate(0, 2, 0));
        expected.add(new Coordinate(0, 6, 0));

        assertEquals(expected, actual);
    }

    public void testDepth1Case() {
        DepthFilter query = new DepthFilter(layer, new ConstantInteger(1));
        List<Coordinate> actual = query.apply(initial);

        List<Coordinate> expected = new ArrayList<>();
        expected.add(new Coordinate(0, 2, 0));
        expected.add(new Coordinate(0, 3, 0));
        expected.add(new Coordinate(0, 6, 0));
        expected.add(new Coordinate(0, 5, 0));

        assertEquals(expected, actual);
    }

    public void testOriginalNotMutated() {
        DepthFilter query = new DepthFilter(layer, new ConstantInteger(0));
        List<Coordinate> expected = new ArrayList<>(initial);
        query.apply(initial);
        List<Coordinate> actual = initial;
        assertFalse(expected == actual);
        assertEquals(expected, actual);
    }
}