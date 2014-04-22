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

package io.serialize.binary;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;
import structural.utilities.FileConventions;
import structural.utilities.PrimitiveSerializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbborens on 3/28/14.
 */
public class HighlightWriter extends Serializer {
    private Geometry geometry;
    private Map<Integer, DataOutputStream> streamMap;

    private int[] channels;

    public HighlightWriter(GeneralParameters p, int[] channels) {
        super(p);
        makeFiles();
        this.channels = channels;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        closeDataStreams();
    }

    @Override
    public void close() {
    }

    @Override
    public void cycleEnd(StepState stepState, int frame) {
        for (int channel : channels) {
            DataOutputStream stream = streamMap.get(channel);
            List<Coordinate> vector = Arrays.asList(stepState.getHighlights(channel));

            PrimitiveSerializer.writeCoercedCoordinateVector(stream, vector, geometry);
        }
    }

    @Override
    public void cycleStart(StepState stepState, int frame) {

    }

    @Override
    public void init(LayerManager layerManager) {
        super.init(layerManager);
        geometry = layerManager.getCellLayer().getGeometry();

        createDataStreams();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HighlightWriter)) {
            return false;
        }

        HighlightWriter other = (HighlightWriter) obj;

        if (other.channels.length != this.channels.length) {
            return false;
        }

        for (int i = 0; i < channels.length; i++) {
            if (other.channels[i] != this.channels[i]) {
                return false;
            }
        }

        return true;
    }

    private void createDataStreams() {
        streamMap = new HashMap<>(channels.length);

        for (Integer channel : channels) {
            String baseFilename = FileConventions.makeHighlightFilename(channel);
            String absoluteName = p.getInstancePath() + baseFilename;
            DataOutputStream stream = FileConventions.makeDataOutputStream(absoluteName);
            streamMap.put(channel, stream);
        }
    }

    private void closeDataStreams() {
        try {
            for (DataOutputStream stream : streamMap.values()) {
                stream.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
