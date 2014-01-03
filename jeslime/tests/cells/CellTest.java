package cells;

import junit.framework.TestCase;
import test.EslimeTestCase;

/**
 * Created by dbborens on 1/2/14.
 */
public abstract class CellTest extends EslimeTestCase {
    public abstract void testGetState() throws Exception;

    public abstract void testGetFitness() throws Exception;

    public abstract void testIsDivisible() throws Exception;

    public abstract void testFeedConsiderApply() throws Exception;

    public abstract void testDivide() throws Exception;

    public abstract void testClone() throws Exception;

    public abstract void testGetProduction() throws Exception;
}
