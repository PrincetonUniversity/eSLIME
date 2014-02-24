package structural;

import junit.framework.TestCase;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/20/14.
 */
public class XmlUtilTest extends EslimeTestCase {
    Element fixtureRoot;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("XmlUtilTest.xml");
    }

    public void testGetBoolean() throws Exception {
        Element testRoot = fixtureRoot.element("get-boolean");
        doBooleanTest(testRoot, "flag-only", true);
        doBooleanTest(testRoot, "no-flag", false);
        doBooleanTest(testRoot, "tag-true", true);
        doBooleanTest(testRoot, "tag-false", false);
    }

    private void doBooleanTest(Element testRoot, String childName, boolean expected) {
        Element child = testRoot.element(childName);
        boolean actual = XmlUtil.getBoolean(child, "test");
        assertEquals(expected, actual);
    }
}
