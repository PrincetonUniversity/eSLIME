/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParameterWriter extends Serializer {
    private final String PROJECT_FILENAME = "project.xml";

    public ParameterWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
        mkDir(p.getPath(), true);

        try {
            String paramsFileStr = p.getPath() + '/' + PROJECT_FILENAME;
            File paramsFile = new File(paramsFileStr);
            FileWriter fw = new FileWriter(paramsFile);
            BufferedWriter bwp = new BufferedWriter(fw);

            bwp.write(p.getProjectXML());
            bwp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init() {
        // Write out an instance-specific XML file.
        if (p.getNumInstances() == 1) {
            return;
        }
        mkDir(p.getInstancePath(), true);

        try {
            String paramsFileStr = p.getInstancePath() + '/' + PROJECT_FILENAME;
            File paramsFile = new File(paramsFileStr);
            FileWriter fw = new FileWriter(paramsFile);
            BufferedWriter bwp = new BufferedWriter(fw);
            bwp.write(p.getInstanceXML());
            bwp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush(StepState stepState) {

    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // Doesn't do anything.

    }

    @Override
    public void close() {
        // Doesn't do anything.

    }

}
