package io.project;

import agent.Behavior;
import agent.action.Action;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import cells.SimpleCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;
import io.project.CellFactory;
/**
 * Created by dbborens on 2/18/14.
 */
public class CellFactoryTest extends EslimeTestCase {
    Element fixtureRoot;
    MockLayerManager layerManager = new MockLayerManager();
    MockGeneralParameters p = new MockGeneralParameters();

    @Override
    protected void setUp() throws Exception {
        fixtureRoot = readXmlFile("CellFactoryTest.xml");
        layerManager = new MockLayerManager();

        MockGeometry geom = buildMockGeometry();
        CellLayer layer = new CellLayer(geom, 0);
        layerManager.setCellLayer(layer);

    }

    public void testSimpleCell() {
        Element fixture = fixtureRoot.element("cell-a");
        CellFactory query = new CellFactory(layerManager, fixture, p);
        Cell actual = query.instantiate();
        Cell expected = new SimpleCell(1);
        assertEquals(expected, actual);
    }

    public void testBehaviorCell() {
        Element fixture = fixtureRoot.element("cell-b");
        CellFactory query = new CellFactory(layerManager, fixture, p);
        Cell actual = query.instantiate();

        // Build the expected cell (no mock, as factory must call correct sub-component factories)
        BehaviorCell expected = new BehaviorCell(layerManager, 2, 0.5, 1.0);
        BehaviorDispatcher expectedDispatcher = new BehaviorDispatcher(expected, layerManager, p);

        Action[] actionList = new Action[] {null};
        Behavior testBehavior = new Behavior(expected, layerManager, actionList);
        expectedDispatcher.map("test-behavior", testBehavior);
        expected.setDispatcher(expectedDispatcher);

        assertEquals(expected, actual);
    }
}
