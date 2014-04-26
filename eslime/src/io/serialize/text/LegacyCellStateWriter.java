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
import control.identifiers.Coordinate;
import control.identifiers.Extrema;
import geometry.Geometry;
import io.serialize.Serializer;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.StepState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Writes the state of the system to a file. To avoid lots of opening and
 * closing of files, as well as lots of panning around the disk, a single
 * file is used for all state.
 * <p/>
 * Since disk is much slower than compute, in the future I might move this
 * to a separate thread, with a queue of data to be appended. However, since
 * that is a somewhat delicate piece of engineering, I will defer that until
 * I see how much optimizing is needed.
 * <p/>
 * In the meantime, this uses the default Java BufferedWriter, which will
 * block the thread every time it needs to write to disk.
 *
 * @author dbborens@princeton.edu
 */
@Deprecated
public class LegacyCellStateWriter extends Serializer {

    private static final double log10 = Math.log(10D);

    // This file contains state vectors, with vector indices corresponding
    // to coordinates. The mapping between index value and coordinate is
    // specified in coordinate.txt.
    private final String STATE_FILENAME = "data.txt";

    private final String METADATA_FILENAME = "metadata.txt";


    // Dictates how many data to write per line.
    private final int DATA_PER_LINE = 20;

    // I/O handle for the state file
    private BufferedWriter stateWriter;

//    // Timestamp for project
//    private Date date = new Date();

    // Extrema for each field type
    private Extrema ef;                // Fitness

    //public CellStateWriter(String stateDir, Parameters p, int n) {

    public LegacyCellStateWriter(GeneralParameters p) {
        super(p);
    }


    public void init(LayerManager layerManager) {
        super.init(layerManager);
        initStructures();
        makeFiles();
        initFiles();
    }


    protected void initFiles() {
        // Create the state & interval files
        String stateFileStr = p.getInstancePath() + '/' + STATE_FILENAME;

        try {

            File stateFile = new File(stateFileStr);
            FileWriter fw = new FileWriter(stateFile);
            stateWriter = new BufferedWriter(fw, 1048576);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private void initStructures() {
        // Initialize extrema
        ef = new Extrema();
    }


    /**
     * Appends a state to the file.
     */
    @Override
    public void flush(StepState stepState) {
        CellLayer layer = stepState.getRecordedCellLayer();
        int[] s = layer.getViewer().getStateVector();
        double[] f = layer.getViewer().getFitnessVector();

            writeDoubleArray(f, layer, ef, stepState.getTime(),
                    stepState.getFrame(), "fitness");

            writeIntegerArray(s, stepState.getTime(), stepState.getFrame(), "state");

    }

    /**
     * Write out the cell types, represented by colors.
     * <p/>
     * The format for cell color is:
     * <p/>
     * >color:0
     * 0123456701234567....
     * 7654321076543210....
     * >color:1
     * ...
     * <p/>
     * Recall that base colors are encoded in binary:
     * Red   += 4
     * Green += 2
     * Blue  += 1
     * <p/>
     * Hence, our visualization is limited to 8 cell
     * types, including null cells (which are black).
     *
     * @param v the verctor of values, in canonical site order.
     */
    private void writeIntegerArray(int[] v, double gillespie, int frame, String name) {
        StringBuilder sb = new StringBuilder(">" + name + ":");
        sb.append(frame);
        sb.append(':');
        sb.append(gillespie);
        sb.append('\n');

        for (int i = 0; i < v.length; i++) {
            sb.append(v[i]);
            if (i % DATA_PER_LINE == DATA_PER_LINE - 1)
                sb.append('\n');
            else if (i == v.length - 1)
                sb.append('\n');
            else
                sb.append('\t');
        }

        try {
            stateWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a vector to the file. The format is as follows:
     * <p/>
     * The format is as follows:
     * <p/>
     * >first_field:0
     * 0.012345	0.012345	0.012345....
     * 0.012345	0.012345	0.012345....
     * >second_field:0
     * 0.012345	0.012345	0.012345....
     * 0.012345	0.012345	0.012345....
     * >first_field:1
     * ...
     * <p/>
     * The delimiters are tabs, and there are p.W() tokens per line.
     *
     * @param v The vector of values, in canonical site order.
     */
    private void writeDoubleArray(double[] v, CellLayer layer, Extrema extrema,
                                  double gillespie, int frame, String title) {

        Coordinate[] coords = layer.getGeometry().getCanonicalSites();

        StringBuilder sb = new StringBuilder();
        ;
        sb.append('>');
        sb.append(title);
        sb.append(':');
        sb.append(frame);
        sb.append(':');
        sb.append(gillespie);
        sb.append("\n");
        for (int i = 0; i < v.length; i++) {
            Double u = v[i];
            extrema.consider(u, coords[i], gillespie);
            sb.append(v[i]);
            if (i % DATA_PER_LINE == DATA_PER_LINE - 1)
                sb.append('\n');
            else if (i == v.length - 1)
                sb.append('\n');
            else
                sb.append('\t');
        }

        try {
            stateWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void conclude() {
        // Close the state data file.
        try {

            stateWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Write the metadata file.
        try {
            File metadata = new File(p.getInstancePath() + '/' + METADATA_FILENAME);
            FileWriter mfw = new FileWriter(metadata);
            BufferedWriter mbw = new BufferedWriter(mfw);

            mbw.write("fitness>");
            mbw.write(ef.toString());
            mbw.write('\n');

            mbw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        // Doesn't do anything.
    }

    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

}
