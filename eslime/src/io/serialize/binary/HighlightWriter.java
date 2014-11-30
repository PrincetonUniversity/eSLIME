/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
//    private Geometry geometry;
    private Map<Integer, DataOutputStream> streamMap;

    private int[] channels;

    public HighlightWriter(GeneralParameters p, int[] channels, LayerManager lm) {
        super(p, lm);
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
    public void flush(StepState stepState) {
        Geometry geometry = stepState.getRecordedCellLayer().getGeometry();
        for (int channel : channels) {
            DataOutputStream stream = streamMap.get(channel);
            List<Coordinate> vector = Arrays.asList(stepState.getHighlights(channel));

            PrimitiveSerializer.writeCoercedCoordinateVector(stream, vector, geometry);
        }
    }

    @Override
    public void init() {
        super.init();

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
