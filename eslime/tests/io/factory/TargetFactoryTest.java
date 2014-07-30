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

package io.factory;

import agent.targets.*;
import factory.agent.targets.TargetFactory;
import junit.framework.TestCase;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

/**
 * Created by dbborens on 2/10/14.
 */
public class TargetFactoryTest extends TestCase {

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

    public void testSetMaximum() {
        Element element = createTargetElement("vacant-neighbors");
        Element max = new BaseElement("max");
        max.setText("2");
        element.add(max);
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, null);
        assertEquals(2, targetRule.getMaximum());
    }

    public void testNoMaximum() {
        Element element = createTargetElement("vacant-neighbors");
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, null);
        assertEquals(-1, targetRule.getMaximum());
    }

    private void doTest(String targetName, Class expected) {
        Element element = createTargetElement(targetName);
        TargetRule targetRule = TargetFactory.instantiate(null, null, element, null);
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
