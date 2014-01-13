package io.project;

import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import layers.cell.CellLayer;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.FlyweightAttribute;
import processes.Process;
import processes.gillespie.GillespieProcess;
import processes.temporal.ExponentialInverse;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;
import processes.*;
import processes.temporal.*;
import processes.continuum.*;
import processes.discrete.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    // I'm not sure why I created this
    private static final String[] RECOGNIZED_PROCESSES = new String[] {
        "",
        "tick",
        "divide-anywhere",
        "active-layer-divide",
        "neighbor-swap",
        "scatter",
        "fill",
        "uniform-biomass-growth",
        "targeted-biomass-growth",
        "mutate-all",
        "gillespie-process",
        "null-process"
    };

    @Override
    protected void setUp() throws Exception {
        loader = new MockProcessLoader();


        geometry = new MockGeometry();

        Coordinate[] canonicalSites = new Coordinate[] {
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
        Element[] children = new Element[] {child};
        doTest(elementName, expected, children);
    }

    private void check(Element query, Class expected) {
        Class actual;
        loader.setElement(0, query);
        actual = processFactory.instantiate(0).getClass();
        assertEquals(expected, actual);
    }

    public void testInstantiate() {
        Element child;
        Element[] children;

        // Exponential inverse
        doTest("exponential-inverse", ExponentialInverse.class);

        // Tick
        child = new BaseElement("dt");
        child.setText("1");
        doTest("tick", Tick.class, child);

        // Divide anywhere
        doTest("divide-anywhere", DivideAnywhere.class);

        // Active layer divide
        child = new BaseElement("depth");
        child.setText("1");
        doTest("active-layer-divide", ActiveLayerDivide.class, child);

        // Neighbor swap
        doTest("neighbor-swap", NeighborSwap.class);

        // Scatter
        children = new Element[] {
                new BaseElement("types"),
                new BaseElement("tokens")
        };
        children[0].setText("1");
        children[1].setText("1");
        doTest("scatter", Scatter.class, children);

        // Fill
        child = new BaseElement("skip-filled-sites");
        child.setText("true");
        doTest("fill", Fill.class, child);

        // Uniform biomass growth
        children = new Element[] {
                new BaseElement("delta"),
                new BaseElement("defer")
        };
        children[0].setText("0.0");
        children[1].setText("true");
        doTest("uniform-biomass-growth", UniformBiomassGrowth.class, children);

        // Targeted biomass growth
        children = new Element[] {
                new BaseElement("delta"),
                new BaseElement("defer"),
                new BaseElement("target")
        };
        children[0].setText("0.0");
        children[1].setText("true");
        children[2].setText("1");
        doTest("targeted-biomass-growth", TargetedBiomassGrowth.class, children);

        // Mutate all
        child = new BaseElement("mutation");

        Attribute ancestral = new FlyweightAttribute(new QName("ancestral"), "1");
        Attribute mutant = new FlyweightAttribute(new QName("mutant"), "2");
        child.add(ancestral);
        child.add(mutant);
        doTest("mutate-all", MutateAll.class, child);

        // Null process
        child = new BaseElement("identifier");
        child.setText("1");
        doTest("null-process", NullProcess.class, child);

        // Gillespie process
        child = new BaseElement("children");
        doTest("gillespie-process", GillespieProcess.class, child);

        // Update field
        child = new BaseElement("target");
        child.setText("test");
        doTest("field-update-process", FieldUpdateProcess.class, child);
    }

    public void testGetProcesses() {
        loader.setProcesses(new Integer[]{0, 1});
        loader.setElement(0, nullProcessElement("0"));
        loader.setElement(1, nullProcessElement("1"));

        Process[] actual = processFactory.getProcesses();
        assertEquals(2, actual.length);
        assertEquals(NullProcess.class, actual[0].getClass());
        assertEquals(NullProcess.class, actual[1].getClass());
    }

    private Element nullProcessElement(String idStr) {
        Element root = new BaseElement("null-process");
        Element identifier = new BaseElement("identifier");
        identifier.setText(idStr);
        root.add(identifier);
        return root;
    }
}
