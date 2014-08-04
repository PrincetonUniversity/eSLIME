/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import control.GeneralParameters;
import control.arguments.ConstantInteger;
import factory.processes.discrete.filter.FilterFactory;
import org.dom4j.Element;
import processes.discrete.filter.*;
import test.EslimeLatticeTestCase;

public class FilterFactoryTest extends EslimeLatticeTestCase {
    private Element root;
    private GeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("./factories/FilterFactoryTest.xml");
        p = makeMockGeneralParameters();
    }

    public void testDefault() throws Exception {
        Element e = root.element("does-not-exist");
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        Filter expected = new NullFilter();
        assertEquals(expected, actual);
    }

    public void testImplicitNullFilter() throws Exception {
        Element e = root.element("implicit-null-case");
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        Filter expected = new NullFilter();
        assertEquals(expected, actual);
    }

    public void testExplicitNullFilter() throws Exception {
        Element e = root.element("explicit-null-case");
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        Filter expected = new NullFilter();
        assertEquals(expected, actual);
    }

    public void testCompositeFilter() throws Exception {
        Element e = root.element("composite-case");

        Filter[] children = new Filter[] {
                new NullFilter(),
                new NullFilter()
        };

        Filter expected = new CompositeFilter(children);
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        assertEquals(expected, actual);
    }

    public void testNestedComposite() throws Exception {
        Element e = root.element("nested-composite-case");

        Filter[] subChildren = new Filter[] { new NullFilter() };

        Filter[] children = new Filter[] {
                new CompositeFilter(subChildren),
                new NullFilter()
        };

        Filter expected = new CompositeFilter(children);
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        assertEquals(expected, actual);
    }

    public void testStateFilter() throws Exception {
        Element e = root.element("state-filter-case");

        Filter expected = new CellStateFilter(null, new ConstantInteger(1));
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        assertEquals(expected, actual);
    }

    public void testDepthFilter() throws Exception {
        Element e = root.element("depth-filter-case");

        Filter expected = new DepthFilter(null, new ConstantInteger(1));
        Filter actual = FilterFactory.instantiate(e, layerManager, p);
        assertEquals(expected, actual);
    }
}