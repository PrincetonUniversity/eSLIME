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

    public ParameterWriter(GeneralParameters p) {
        super(p);
        mkDir(p.getPath(), true);

        try {
            String paramsFileStr = p.getPath() + '/' + PROJECT_FILENAME;
            File paramsFile = new File(paramsFileStr);
            FileWriter fw = new FileWriter(paramsFile);
            BufferedWriter bwp = new BufferedWriter(fw);
            System.out.println("p xml is null? " + (p.getProjectXML() == null));

            bwp.write(p.getProjectXML());
            bwp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init(LayerManager lm) {
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
    public void step(StepState stepState, int frame) {
        // Doesn't do anything.

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
