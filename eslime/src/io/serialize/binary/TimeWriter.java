/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.binary;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;
import structural.utilities.FileConventions;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by dbborens on 3/28/14.
 */
public class TimeWriter extends Serializer {

    private DataOutputStream stream;

    public TimeWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
        makeFiles();
    }

    @Override
    public void init() {
        super.init();
        String path = makeFilePath();
        stream = FileConventions.makeDataOutputStream(path);
    }

    private String makeFilePath() {
        String baseFileName = FileConventions.TIME_FILENAME;
        String absoluteName = p.getInstancePath() + baseFileName;
        return absoluteName;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void flush(StepState stepState) {
        try {
            stream.writeInt(stepState.getFrame());
            stream.writeDouble(stepState.getTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
