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

import cells.MockCell;
import geometry.MockGeometry;
import io.project.MockProcessLoader;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import processes.gillespie.GillespieState;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 2/18/14.
 */
public class TriggerTest extends EslimeTestCase {

    private Trigger trigger;
    private CellLayer layer;
    private MockLayerManager layerManager;
    private MockGeneralParameters p;
    @Override
    protected void setUp() throws Exception {
        MockGeometry geom = buildMockGeometry();

        p = new MockGeneralParameters();
        p.initializeRandom(0);
        layer = new CellLayer(geom, 0);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        trigger = new Trigger(layerManager, "test", p, true);
    }

    /**
     * Make sure that the "target" method works as expected when
     * using Gillespie mode.
     */
    public void testTargetGillespie() throws Exception {
        Integer[] keys = new Integer[]{0};
        GillespieState state = new GillespieState(keys);

        // Gillespie state should be updated by target

        trigger.target(state);
        state.close();
        assertEquals(1, state.getTotalCount());
        assertEquals(1.0, state.getTotalWeight(), epsilon);
    }

    public void testLifeCycle() throws Exception {
        MockCell cell = new MockCell();
        Coordinate c = layer.getGeometry().getCanonicalSites()[0];
        layer.getUpdateManager().place(cell, c);
        trigger.target(null);
        trigger.fire(null);
        assertEquals("test", cell.getLastTriggeredBehaviorName());
        assertNull(cell.getLastTriggeredCaller());
    }

    public void testXmlConstructor() {
        Element base = new BaseElement("trigger");
        Element behaviorName = new BaseElement("behavior");
        behaviorName.setText("test-behavior");
        base.add(behaviorName);
        Element skipVacant = new BaseElement("skip-vacant-sites");
        base.add(skipVacant);

        MockProcessLoader loader = new MockProcessLoader();
        loader.setElement(0, base);

        Trigger actual = new Trigger(loader, layerManager, 0, null, -1);
        Trigger expected = new Trigger(layerManager, "test-behavior", p, true);

        assertEquals(expected, actual);
    }
}
