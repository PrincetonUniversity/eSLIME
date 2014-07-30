/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;


import control.identifiers.Coordinate;

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
