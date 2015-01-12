/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseVector;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ContinuumLayerContentTest extends LinearMocks {

    private ContinuumLayerContent query;

    @Before
    public void init() {
        query = new ContinuumLayerContent(indexer);
        DenseVector vector = vector(1.0, 2.0, 3.0);
        query.setState(vector);
    }

    @Test
    public void get() {
        assertEquals(1.0, query.get(a), epsilon);
    }

    @Test
    public void reset() {
        query.reset();
        assertNull(query.getState());
    }
}