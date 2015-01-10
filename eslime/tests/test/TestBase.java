/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package test;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import org.junit.Before;
import structural.utilities.EpsilonUtil;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class TestBase {

    protected double epsilon;

    protected static void assertVectorsEqual(Vector expected, Vector actual, double tolerance) {
        assertEquals(expected.size(), actual.size());

        IntStream.range(0, expected.size())
                .boxed()
                .forEach(i -> assertEquals(expected.get(i), actual.get(i), tolerance));
    }

    protected static void assertMatricesEqual(Matrix p, Matrix q, double tolerance) {
        assertEquals(p.numColumns(), q.numColumns());
        assertEquals(p.numRows(), q.numRows());

        IntStream.range(0, p.numRows())
                .forEach(i -> IntStream.range(0, p.numColumns())
                        .forEach(j ->
                                assertEquals(p.get(i, j), q.get(i, j), tolerance)));
    }

    @Before
    public void calcEpsilon() {
        epsilon = EpsilonUtil.epsilon();
    }
}
