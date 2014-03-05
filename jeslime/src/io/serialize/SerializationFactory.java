/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package io.serialize;

import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;

/**
 * Created by dbborens on 1/17/14.
 */
public abstract class SerializationFactory {
    public static Serializer instantiate(Element e, GeneralParameters p) {

        String writerClass = e.getName();

        // Cell writers
        if (writerClass.equalsIgnoreCase("cell-state-writer")) {
            CellStateWriter bsw = new CellStateWriter(p);
            return bsw;
        } else if (writerClass.equalsIgnoreCase("fixation-time")) {
            FixationTimeWriter ftw = new FixationTimeWriter(p);
            return ftw;
        } else if (writerClass.equalsIgnoreCase("parameter-writer")) {
            ParameterWriter pw = new ParameterWriter(p);
            return pw;
        } else if (writerClass.equalsIgnoreCase("progress-reporter")) {
            ProgressReporter pr = new ProgressReporter(p);
            return pr;
        } else if (writerClass.equalsIgnoreCase("frequency-writer")) {
            FrequencyWriter freq = new FrequencyWriter(p);
            return freq;
        } else if (writerClass.equalsIgnoreCase("interval-writer")) {
            IntervalWriter iw = new IntervalWriter(p);
            return iw;

        // General writers
        } else if (writerClass.equalsIgnoreCase("coordinate-indexer")) {
            CoordinateIndexer ce = new CoordinateIndexer(p);
            return ce;

        } else if (writerClass.equalsIgnoreCase("continuum-state-writer")) {
            ContinuumStateWriter csw = new ContinuumStateWriter(p);
            return csw;
        } else {
            throw new IllegalArgumentException("Unrecognized serialization '" + writerClass + "'");
        }

    }
}
