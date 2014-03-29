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

package io.deserialize;


import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * This isn't really a mock coordinate de-indexer, so much as an in-memory
 * one. Eventually, it may get a promotion.
 * <p/>
 * Created by David B Borenstein on 3/25/14.
 */
public class MockCoordinateDeindexer extends CoordinateDeindexer {
    private Coordinate[] underlying;

    public MockCoordinateDeindexer() {
        super();
    }

    public void setUnderlying(Coordinate[] underlying) {
        this.underlying = underlying;
        deindex();
    }


    @Override
    protected void deindex() {
        indexToCoord = new HashMap<>();
        coordToIndex = new HashMap<>();


        for (Integer i = 0; i < underlying.length; i++) {
            Coordinate c = underlying[i];
            indexToCoord.put(i, c);
            coordToIndex.put(c, i);
        }

    }
}
