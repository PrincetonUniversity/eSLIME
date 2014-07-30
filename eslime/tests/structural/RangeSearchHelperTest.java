package structural;

import test.EslimeTestCase;

import java.util.ArrayList;

/**
 * The RangeSearchHelper performs a binary search between sequential
 * bins. This is used as part of the RangeMap object. The argument
 * is an array of bin floors, which has an extra element representing
 * the hypothetical floor of one more bin (ie, the ceiling of the last
 * bin).
 * <p/>
 * Created by David B Borenstein on 3/19/14.
 */
public class RangeSearchHelperTest extends EslimeTestCase {

    private RangeSearchHelper query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<Double> bins = new ArrayList<>();
        // Remember that a dummy bin of 0.0 is required...
        bins.add(0.0);
        bins.add(0.5);
        bins.add(1.5);
        bins.add(2.0);
        query = new RangeSearchHelper(bins);
    }

    public void testFindKey() throws Exception {
        int actual;

        // First bin
        actual = query.findKey(0.25);
        assertEquals(0, actual);

        // Second bin
        actual = query.findKey(1.0);
        assertEquals(1, actual);

        // Third bin
        actual = query.findKey(1.75);
        assertEquals(2, actual);
    }

    public void testLowerBoundInclusivity() throws Exception {
        int actual;

        // First bin
        actual = query.findKey(0.0);
        assertEquals(0, actual);

        // Second bin
        actual = query.findKey(0.5);
        assertEquals(1, actual);

        // Third bin
        actual = query.findKey(1.5);
        assertEquals(2, actual);
    }

    /**
     * Verify that a single bin case works fine. Regression
     * test for problem case.
     *
     * @throws Exception
     */
    public void testOneBinRegression() throws Exception {
        ArrayList<Double> bins = new ArrayList<>();
        bins.add(0.0);
        bins.add(0.5);

        query = new RangeSearchHelper(bins);
        int actual = query.findKey(0.25);
        assertEquals(0, actual);
    }
}
