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

package io.serialize;

import control.GeneralParameters;
import control.halt.HaltCondition;
import layers.LayerManager;
import processes.StepState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dbborens on 12/11/13.
 */
public abstract class Serializer {
    protected LayerManager layerManager;
    protected GeneralParameters p;
    protected boolean closed = true;

    public Serializer(GeneralParameters p) {
        this.p = p;
    }

    public void init(LayerManager layerManager) {
        if (!closed) {
            throw new IllegalStateException("Attempting to initialize active writer!");
        }

        this.layerManager = layerManager;
    }


    /**
     * Signal that the current instance is concluded. Conclude analysis, finish
     * writing to files, and close handles for instance.
     */
    public abstract void dispatchHalt(HaltCondition ex);

    protected void makeFiles() {
        // Create the directory for state files, if needed
        mkDir(p.getPath(), true);

        mkDir(p.getInstancePath(), true);

        System.out.println(p.getInstancePath());
    }

    /**
     * Conclude analysis, finish writing to files, and close for entire project.
     */
    public abstract void close();

    /**
     * Step to the next frame of the current instance.
     */
    public abstract void step(StepState stepState, int frame);

    protected void mkDir(String pathStr, boolean recursive) {
        File path = new File(pathStr);
        if (!path.exists()) {
            try {
                if (recursive)
                    path.mkdirs();
                else
                    path.mkdir();
            } catch (Exception ex) {
                System.out.println("Could not create directory tree " + pathStr);
                throw new RuntimeException(ex);
            }
        }
    }

    protected BufferedWriter makeBufferedWriter(String filename) {
        BufferedWriter bw;
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return bw;
    }

    /**
     * Convenience method for appending the contents of a string builder
     * to a buffered writer, converting any IO exceptions to runtime
     * exceptions.
     *
     * @param bw
     * @param line
     */
    protected void hAppend(BufferedWriter bw, StringBuilder line) {
        try {
            bw.append(line.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void hClose(BufferedWriter bw) {
        try {
            bw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
