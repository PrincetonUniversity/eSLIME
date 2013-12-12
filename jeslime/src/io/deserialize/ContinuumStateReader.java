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
    private String filename;

    private ContinuumState current;

    public ContinuumStateReader(File filename) {
        // Open the file.
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
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
        } catch(IOException ex) {
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
        // Length is encoded, redundantly, in each vector.
        int length = input.readInt();

        //System.out.println("I think length is " + length);
        DenseVector ret = new DenseVector(length);
        for (int i = 0; i < length; i++) {
            double value = input.readDouble();
            ret.set(i, value);
        }

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
                throw new IOException("Continuum state file '" + filename + "' is corrupt.");
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
                throw new IOException("Continuum state file '" + filename + "' is corrupt.");
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
