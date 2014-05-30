/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.factory;

import control.GeneralParameters;
import control.arguments.Argument;
import io.serialize.SerializationManager;
import io.serialize.Serializer;
import io.serialize.binary.ContinuumStateWriter;
import io.serialize.binary.HighlightWriter;
import io.serialize.binary.TimeWriter;
import io.serialize.binary.VisualizationSerializer;
import io.serialize.interactive.ProgressReporter;
import io.serialize.text.*;
import io.visual.Visualization;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbborens on 1/17/14.
 */
public abstract class SerializationFactory {
    public static Serializer instantiate(Element e, GeneralParameters p) {

        String writerClass = e.getName();

        // Cell writers
        if (writerClass.equalsIgnoreCase("cell-state-writer")) {
            LegacyCellStateWriter bsw = new LegacyCellStateWriter(p);
            return bsw;
        } else if (writerClass.equalsIgnoreCase("halt-time-writer")) {
            HaltTimeWriter ftw = new HaltTimeWriter(p);
            return ftw;
        } else if (writerClass.equalsIgnoreCase("parameter-writer")) {
            ParameterWriter pw = new ParameterWriter(p);
            return pw;
        } else if (writerClass.equalsIgnoreCase("progress-reporter")) {
            ProgressReporter pr = new ProgressReporter(p);
            return pr;
        } else if (writerClass.equalsIgnoreCase("census-writer")) {
            CensusWriter freq = new CensusWriter(p);
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
        } else if (writerClass.equalsIgnoreCase("time-writer")) {
            TimeWriter tw = new TimeWriter(p);
            return tw;
        } else if (writerClass.equalsIgnoreCase("highlight-writer")) {
            Element channelsElem = e.element("channels");
            int[] channels = XmlUtil.getIntegerArray(channelsElem, "channel");
            HighlightWriter hw = new HighlightWriter(p, channels);
            return hw;
        } else if (writerClass.equalsIgnoreCase("visualization-serializer")) {
            VisualizationSerializer vs = visualizationSerializer(e, p);
            return vs;
        } else if (writerClass.equalsIgnoreCase("correlation-writer")) {
            return correlationWriter(e, p);
        } else {
            throw new IllegalArgumentException("Unrecognized serialization '" + writerClass + "'");
        }
    }

    public static SerializationManager makeManager(Element we, GeneralParameters p) {
        List<Serializer> writers = new ArrayList<>();

        for (Object o : we.elements()) {
            Element e = (Element) o;
            Serializer w = SerializationFactory.instantiate(e, p);
            writers.add(w);
        }

        SerializationManager manager = new SerializationManager(p, writers);
        return manager;
    }

    private static CorrelationWriter correlationWriter(Element e, GeneralParameters p) {
        Argument<Double> triggerTimeArg = DoubleArgumentFactory.instantiate(e, "trigger-time", 0.0, p.getRandom());
        String filename = XmlUtil.getString(e, "filename", "correlation.txt");
        return new CorrelationWriter(p, filename, triggerTimeArg);
    }
    private static VisualizationSerializer visualizationSerializer(Element e,
                                                                   GeneralParameters p) {

        String prefix;
        Element prefixElement = e.element("prefix");
        if (prefixElement == null) {
            prefix = "frame";
        } else {
            prefix = prefixElement.getTextTrim();
        }

        Element visElement = e.element("visualization");
        Visualization visualization = VisualizationFactory.instantiate(visElement);

        return new VisualizationSerializer(p, visualization, prefix);
    }
}
