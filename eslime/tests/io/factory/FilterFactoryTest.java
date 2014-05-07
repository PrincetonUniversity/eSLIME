/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.factory;

import control.GeneralParameters;
import control.arguments.ConstantInteger;
import junit.framework.TestCase;
import org.dom4j.Element;
import processes.discrete.filter.CellStateFilter;
import processes.discrete.filter.CompositeFilter;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;
import test.EslimeLatticeTestCase;
import test.EslimeTestCase;

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
}