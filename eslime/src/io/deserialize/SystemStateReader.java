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

import layers.LightweightSystemState;
import layers.SystemState;

import java.util.Iterator;

/**
 * Created by dbborens on 3/26/14.
 */
public class SystemStateReader implements Iterable<SystemState> {

    // A list of all time points measured in the data files, as measured
    // in system time.
    private double[] times;

    // The time points in times correspond to some number of cycles through
    // the simulation (frames).
    private int[] frames;
    // Coordinate deindexers convert from a numeric index to a coordinate.
    private CoordinateDeindexer deindexer;

    // Where are we in the time series being loaded?
    private int cursor;

    /* Data handles */
    private HighlightReader highlightReader;
    private LegacyCellStateReader cellStateReader;
    private ContinuumStateReaderManager continuumStateReaderManager;

    public SystemStateReader(String[] soluteIds, int[] channelIds, String fileRoot) {
        cursor = 0;

        // Load coordinate de-indexer.
        deindexer = new CoordinateDeindexer(fileRoot);

        // Load frame-time linkage file.
        loadTime(fileRoot);

        // Open ContinuumStateReader object for each continuum field.
        continuumStateReaderManager = new ContinuumStateReaderManager(fileRoot, soluteIds);

        // Open handle to data file for each highlght channel.
        highlightReader = new HighlightReader(fileRoot, channelIds, deindexer);

        // Open handle to data file for cell state vector.
        cellStateReader = new LegacyCellStateReader(fileRoot, deindexer);
    }


    private void loadTime(String fileRoot) {
        TimeReader timeReader = new TimeReader(fileRoot);
        times = timeReader.getTimes();
        frames = timeReader.getFrames();
    }


    private void setTimeAndFrame(LightweightSystemState state) {
        double time = times[cursor];
        int frame = frames[cursor];
        state.setTime(time);
        state.setFrame(frame);
    }

    @Override
    public Iterator<SystemState> iterator() {
        return new SystemStateIterator();
    }

    private class SystemStateIterator implements Iterator<SystemState> {

        @Override
        public LightweightSystemState next() {

            // Construct display object
            LightweightSystemState state = new LightweightSystemState(deindexer);

            // Populate time and frame
            setTimeAndFrame(state);

            // Populate cell states
            cellStateReader.populate(state);

            // Populate state of continuum fields
            continuumStateReaderManager.populate(state);

            // Populate cell change highlights
            highlightReader.populate(state);

            // Advance the cursor
            cursor++;

            // Return display object
            return state;
        }

        public boolean hasNext() {
            return (cursor < frames.length);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
