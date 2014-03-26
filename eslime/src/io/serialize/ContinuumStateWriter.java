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

import layers.LayerManager;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;

import java.io.*;

/**
 * Created by dbborens on 12/11/13.
 * <p/>
 * ContinuumStateWriter encodes a binary file containing the
 * state of the model at specified time points.
 */
public class ContinuumStateWriter extends Serializer {

    private SoluteLayer layer;
    private Extrema extrema;

    private static String prefix = "solute";
    private static String stateSuffix = ".state.bin";
    private static String mdSuffix = ".metadata.txt";

    private DataOutputStream dataStream;

    // Canonical sites (for this instance)
    private Coordinate[] sites;


    public ContinuumStateWriter(GeneralParameters p) {
        super(p);
    }

    @Override
    public void init(LayerManager layerManager) {
        super.init(layerManager);

        if (layerManager.getSoluteLayers().length != 1) {
            throw new UnsupportedOperationException("Support not yet implemented for multiple solute layer serialization. To do this, all you need to do is turn all of the state variables into maps of ID --> whatever.");
        }

        // We currently get an array of solute layers, but that array had better only have
        // one thing in it until multi-layer support is introduced.
        layer = layerManager.getSoluteLayers()[0];

        initStructures();

        makeFiles();

        initFiles();
    }

    private void initFiles() {
        String filename = makeFileName();
        String filepath = p.getInstancePath() + '/' + filename;

        try {

            File stateFile = new File(filepath);

            FileOutputStream fileOutputStream = new FileOutputStream(stateFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            dataStream = new DataOutputStream(bufferedOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(layer.getId());
        sb.append(stateSuffix);
        return sb.toString();
    }

    public void initStructures() {
        sites = layer.getGeometry().getCanonicalSites();

        // Initialize extrema
        extrema = new Extrema();
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        try {
            // Write opening parity sequence
            writeStartParitySequence();

            // Write entry header
            dataStream.writeDouble(gillespie);
            dataStream.writeInt(frame);

            // Process state vector
            processData(frame);

            // Write closing parity sequence
            writeEndParitySequence();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Write the state vector to the data stream and update extrema.
     *
     * @param frame
     * @throws IOException
     */
    private void processData(int frame) throws IOException {
        DenseVector data = layer.getState().getSolution();
        updateExtrema(data, frame);

        PrimitiveSerializer.writeDoubleVector(dataStream, data.getData());
    }

    private void updateExtrema(DenseVector data, int frame) {
        for (int i = 0; i < data.size(); i++) {
            double datum = data.get(i);
            extrema.consider(datum, sites[i], frame);
        }
    }

    /**
     * Encode parity sequence for entry start
     */
    private void writeStartParitySequence() throws IOException {
        for (int i = 0; i < 2; i++) {
            dataStream.writeBoolean(true);
        }
    }

    /**
     * Encode parity sequence for entry end
     */
    private void writeEndParitySequence() throws IOException {
        for (int i = 0; i < 2; i++) {
            dataStream.writeBoolean(false);
        }
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

    private void conclude() {
        // Close the state data file.
        try {
            dataStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeMetadata();

    }

    private void writeMetadata() {
        // Write the extrema file.
        try {
            File metadata = new File(getMetadataFilename());
            String filepath = p.getInstancePath() + '/' + metadata;
            FileWriter mfw = new FileWriter(filepath);
            BufferedWriter mbw = new BufferedWriter(mfw);

            mbw.write("extrema>");
            mbw.write(extrema.toString());
            mbw.write('\n');

            mbw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMetadataFilename() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(layer.getId());
        sb.append(mdSuffix);
        return sb.toString();
    }

    @Override
    public void close() {
        // Doesn't do anything
    }

}
