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

import org.dom4j.Element;
import processes.discrete.filter.CompositeFilter;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;

/**
 * Created by dbborens on 5/5/14.
 */
public abstract class FilterFactory {

    public static Filter instantiate(Element e) {
        // No arguments? No problem; we take it to mean the the user didn't
        // want any filters. Similarly, if there are no child elements, then
        // the default filter is applied.
        if (e == null || e.elements() == null) {
            return instantiateDefault();
        }

        int nChildren = e.elements().size();

        if (nChildren == 0) {
            return new NullFilter();
        } else {
            return compositeFilter(e);
        }
    }

    private static Filter instantiateDefault() {
        return new NullFilter();
    }

    private static Filter compositeFilter(Element e) {
        int nChildren = e.elements().size();
        Filter[] children = new Filter[nChildren];

        int i = 0;
        for (Object o : e.elements()) {
            Filter child = processChild((Element) o);
            children[i] = child;
            i++;
        }

        CompositeFilter ret = new CompositeFilter(children);
        return ret;
    }

    private static Filter processChild(Element e) {
        String name = e.getName();

        if (name.equalsIgnoreCase("null")) {
            return new NullFilter();
        } else if (name.equalsIgnoreCase("composite")) {
            return compositeFilter(e);
        } else {
            String msg = "Unrecognized filter '" + name + "'.";
            throw new IllegalArgumentException(msg);
        }
    }
}
