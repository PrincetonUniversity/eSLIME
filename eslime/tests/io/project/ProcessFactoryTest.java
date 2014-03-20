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

package io.project;

import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import layers.cell.CellLayer;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.FlyweightAttribute;
import processes.MockProcess;
import processes.Process;
import processes.continuum.FieldUpdateProcess;
import processes.discrete.*;
import processes.gillespie.GillespieProcess;
import processes.temporal.ExponentialInverse;
import processes.temporal.Tick;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class ProcessFactoryTest extends TestCase {
    private ProcessFactory processFactory;
    private MockProcessLoader loader;
    private MockLayerManager lm;
    private MockGeneralParameters p;
    private MockSoluteLayer sl;
    private CellLayer cl;
    private MockGeometry geometry;

    @Override
    protected void setUp() throws Exception {
        loader = new MockProcessLoader();


        geometry = new MockGeometry();

        Coordinate[] canonicalSites = new Coordinate[]{
                new Coordinate(0, 0, 0)
        };

        geometry.setCanonicalSites(canonicalSites);

        cl = new CellLayer(geometry, 0);

        sl = new MockSoluteLayer();
        sl.setGeometry(geometry);

        lm = new MockLayerManager();
        lm.setCellLayer(cl);
        lm.addSoluteLayer("test", sl);

        p = new MockGeneralParameters();

        processFactory = new ProcessFactory(loader, lm, p);
    }

    private void doTest(String elementName, Class expected) {
        Element query = new BaseElement(elementName);
        check(query, expected);
    }

    private void doTest(String elementName, Class expected, Element[] children) {
        Element query = new BaseElement(elementName);
        for (Element e : children) {
            query.add(e);
        }
        check(query, expected);
    }

    private void doTest(String elementName, Class expected, Element child) {
        Element[] children = new Element[]{child};
        doTest(elementName, expected, children);
    }

    private void check(Element query, Class expected) {
        Class actual;
        loader.setElement(0, query);
        actual = processFactory.instantiate(0).getClass();
        assertEquals(expected, actual);
    }

    public void testExponentialInverse() {
        doTest("exponential-inverse", ExponentialInverse.class);
    }

    public void testTick() {
        Element child = new BaseElement("dt");
        child.setText("1");
        doTest("tick", Tick.class, child);

    }

    public void testDivideAnywhere() {
        doTest("divide-anywhere", DivideAnywhere.class);
    }

    public void testActiveLayerDivide() {
        Element child = new BaseElement("depth");
        child.setText("1");
        doTest("active-layer-divide", ActiveLayerDivide.class, child);

    }

    public void testNeighborSwap() {
        doTest("neighbor-swap", NeighborSwap.class);
    }

    public void testScatter() {
        Element[] children = new Element[]{
                new BaseElement("types"),
                new BaseElement("tokens")
        };
        children[0].setText("1");
        children[1].setText("1");
        doTest("scatter", Scatter.class, children);
    }

    public void testFill() {
        Element child = new BaseElement("skip-filled-sites");
        child.setText("true");
        doTest("fill", Fill.class, child);

    }

    public void testUniformBiomassGrowth() {
        Element[] children = new Element[]{
                new BaseElement("delta"),
                new BaseElement("defer")
        };
        children[0].setText("0.0");
        children[1].setText("false");
        doTest("uniform-biomass-growth", UniformBiomassGrowth.class, children);

    }

    public void testTargetedBiomassGrowth() {
        Element[] children = new Element[]{
                new BaseElement("delta"),
                new BaseElement("defer"),
                new BaseElement("target")
        };
        children[0].setText("0.0");
        children[1].setText("false");
        children[2].setText("1");
        doTest("targeted-biomass-growth", TargetedBiomassGrowth.class, children);
    }

    public void testMutateAll() {
        Element child = new BaseElement("mutation");

        Attribute ancestral = new FlyweightAttribute(new QName("ancestral"), "1");
        Attribute mutant = new FlyweightAttribute(new QName("mutant"), "2");
        child.add(ancestral);
        child.add(mutant);
        doTest("mutate-all", MutateAll.class, child);
    }

    public void testMockProcess() {
        Element child = new BaseElement("identifier");
        child.setText("1");
        doTest("mock-process", MockProcess.class, child);

    }

    public void testGillespieProcess() {
        Element child = new BaseElement("children");
        doTest("gillespie-process", GillespieProcess.class, child);
    }

    public void testFieldUpdateProcess() {
        Element child = new BaseElement("target");
        child.setText("test");
        doTest("field-update-process", FieldUpdateProcess.class, child);

    }

    public void testTrigger() {
        Element[] children = new Element[]{
                new BaseElement("behavior"),
                new BaseElement("skip-vacant-sites")
        };

        children[0].setText("test");
        doTest("trigger", TriggerProcess.class, children);
    }

    public void testGetProcesses() {
        loader.setProcesses(new Integer[]{0, 1});
        loader.setElement(0, nullProcessElement("0"));
        loader.setElement(1, nullProcessElement("1"));

        Process[] actual = processFactory.getProcesses();
        assertEquals(2, actual.length);
        assertEquals(MockProcess.class, actual[0].getClass());
        assertEquals(MockProcess.class, actual[1].getClass());
    }

    private Element nullProcessElement(String idStr) {
        Element root = new BaseElement("mock-process");
        Element identifier = new BaseElement("identifier");
        identifier.setText(idStr);
        root.add(identifier);
        return root;
    }
}
