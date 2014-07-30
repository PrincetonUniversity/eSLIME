/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import control.identifiers.Coordinate;
import control.identifiers.Extrema;
import io.serialize.text.ExtremaHelper;
import test.EslimeTestCase;

import java.io.StringWriter;

/**
 * Created by dbborens on 12/11/13.
 */
public class ExtremaHelperTest extends EslimeTestCase {

    private StringWriter sw;
    private ExtremaHelper helper;

    @Override
    protected void setUp() throws Exception {
        sw = new StringWriter();
        helper = new ExtremaHelper(sw);
    }

    public void testPush() throws Exception {
        Extrema a = new Extrema();
        a.consider(0.0, new Coordinate(0, 0, 0, 0), 2.0);
        a.consider(7.0, new Coordinate(1, 0, 0, 0), 1.0);
        helper.push("a", a);

        String expected = "a>0.0@(0, 0, 0 | 0 | 2.0):7.0@(1, 0, 0 | 0 | 1.0)";
        String actual = sw.toString();

        assertEquals(expected, actual);
    }
}
