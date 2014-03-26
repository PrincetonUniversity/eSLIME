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

package io.deserialize;

import no.uib.cipr.matrix.DenseVector;
import structural.ContinuumState;

import java.io.*;
import java.util.Iterator;

/**
 * Created by dbborens on 12/11/13.
 */
public class ContinuumStateReader implements Iterator<ContinuumState> {

    private DataInputStream input;

    private ContinuumState current;

    public ContinuumStateReader(File file) {
        // Open the file.
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            input = new DataInputStream(bufferedInputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Load the first record.
        advance();
    }

    /**
     * Advances the cursor by one record and loads that
     * record into the "current" field.
     */
    private void advance() {
        try {
            // Typical case -- read a record and load it
            ContinuumState observed = read();
            current = observed;
        } catch (EOFException ex) {
            // End of file? Fine, stop
            current = null;
        } catch (IOException ex) {
            // Some other I/O error? Throw it
            throw new RuntimeException(ex);
        }

    }

    @Override
    public boolean hasNext() {
        // If the last attempt to load a record resulted in a null record,
        // we have reached the end of the file; there is no next record.
        if (current == null) {
            return false;
        }

        return true;
    }


    @Override
    public ContinuumState next() {
        // Store the current record for return
        ContinuumState ret = current;

        // Advance to the next record
        advance();

        // Return the stored record
        return ret;
    }

    private ContinuumState read() throws IOException {
        // Check start sequence
        checkStartParitySequence();

        // Read header information
        double gillespie = input.readDouble();
        int frame = input.readInt();

        // Read data sequence
        DenseVector data = readData();

        // Check end sequence
        checkEndParitySequence();

        // Construct state object
        ContinuumState ret = new ContinuumState(data, gillespie, frame);

        // Return state object
        return ret;
    }

    private DenseVector readData() throws IOException {
        double[] data = PrimitiveDeserializer.readDoubleVector(input);
        DenseVector ret = new DenseVector(data);
        return ret;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks the starting parity sequence for a record, and throws an exception if the
     * parity sequence is incorrect.
     *
     * @throws IOException
     */
    private void checkStartParitySequence() throws IOException {
        // The starting parity sequence is two 1 bits.
        for (int i = 0; i < 2; i++) {
            boolean obs = input.readBoolean();
            if (!obs) {
                throw new IOException("Continuum state file is corrupt.");
            }
        }
    }

    /**
     * Checks the ending parity sequence for a record, and throws an exception if the
     * parity sequence is incorrect.
     *
     * @throws IOException
     */
    private void checkEndParitySequence() throws IOException {
        // The ending parity sequence is two 0 bits.
        for (int i = 0; i < 2; i++) {
            boolean obs = input.readBoolean();
            if (obs) {
                throw new IOException("Continuum state file is corrupt.");
            }
        }
    }

    /**
     * Close the input stream.
     */
    public void close() {
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
