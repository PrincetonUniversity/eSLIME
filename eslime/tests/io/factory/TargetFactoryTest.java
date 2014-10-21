/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import agent.targets.*;
import control.arguments.ConstantInteger;
import factory.agent.targets.TargetFactory;
import geometry.Geometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import processes.discrete.filter.CellStateFilter;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/10/14.
 */
public class TargetFactoryTest extends EslimeTestCase {

    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
    }

    public void testTargetAllNeighbors() {
        doTest("all-neighbors", TargetAllNeighbors.class);
    }

    public void testTargetOccupiedNeighbors() {
        doTest("occupied-neighbors", TargetOccupiedNeighbors.class);
    }

    public void testTargetVacantNeighbors() {
        doTest("vacant-neighbors", TargetVacantNeighbors.class);
    }

    public void testTargetCaller() {
        doTest("caller", TargetCaller.class);
    }

    public void testTargetSelf() {
        doTest("self", TargetSelf.class);
    }

    public void testLoadFilter() {
        Geometry geom = makeLinearGeometry(0);
        CellLayer cellLayer = new CellLayer(geom);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(cellLayer);

        /*
            <self>
               <filters>
                  <cell-state>
                     <state>1</state>
                  </cell-state>
               </filters>
            </self>
         */
        Filter expFilter = new CellStateFilter(cellLayer, new ConstantInteger(1));
        Element element = createTargetElement("self");
        Element filterElem = new BaseElement("filters");
        Element classElem = new BaseElement("cell-state");
        Element stateElem = new BaseElement("state");
        stateElem.setText("1");
        filterElem.add(classElem);
        classElem.add(stateElem);
        element.add(filterElem);

        TargetRule actual = TargetFactory.instantiate(null, layerManager, element, p);
        TargetRule expected = new TargetSelf(null, layerManager, expFilter, -1, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testDefaultNullFilter() {
        Geometry geom = makeLinearGeometry(0);
        CellLayer cellLayer = new CellLayer(geom);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(cellLayer);
        Filter expFilter = new NullFilter();
        Element element = createTargetElement("self");
        TargetRule actual = TargetFactory.instantiate(null, layerManager, element, p);
        TargetRule expected = new TargetSelf(null, layerManager, expFilter, -1, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testSetMaximum() {
        Element element = createTargetElement("vacant-neighbors");
        Element max = new BaseElement("max");
        max.setText("2");
        element.add(max);
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, p);
        assertEquals(2, targetRule.getMaximum());
    }

    public void testNoMaximum() {
        Element element = createTargetElement("vacant-neighbors");
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, p);
        assertEquals(-1, targetRule.getMaximum());
    }

    private void doTest(String targetName, Class expected) {
        Element element = createTargetElement(targetName);
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, p);
        Class actual = targetRule.getClass();
        assertEquals(expected, actual);
    }


    private Element createTargetElement(String text) {
        BaseElement element = new BaseElement("target");
        BaseElement className = new BaseElement("class");
        className.setText(text);
        element.add(className);
        return element;
    }

}
