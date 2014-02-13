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

    private void doTest(String targetName, Class expected) {
        Element element = createTargetElement(targetName);
        TargetRule targetRule = TargetFactory.instantiate(null, null, element);
        Class actual = targetRule.getClass();
        assertEquals(expected, actual);
    }


    private Element createTargetElement(String text) {
        BaseElement element = new BaseElement("target");
        element.setText(text);
        return element;
    }

}
