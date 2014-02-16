package io.project;

import agent.targets.*;
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
