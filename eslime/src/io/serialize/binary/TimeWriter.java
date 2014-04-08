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
import io.serialize.Serializer;
import layers.LayerManager;
import structural.utilities.FileConventions;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by dbborens on 3/28/14.
 */
public class TimeWriter extends Serializer {

    private DataOutputStream stream;

    public TimeWriter(GeneralParameters p) {
        super(p);
        makeFiles();
    }

    @Override
    public void init(LayerManager layerManager) {
        super.init(layerManager);
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
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        try {
            stream.writeInt(frame);
            stream.writeDouble(gillespie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
