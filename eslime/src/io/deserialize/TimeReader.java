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

package io.deserialize;

import structural.utilities.FileConventions;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads out frames and the associated system time from
 * a time data file.
 * <p/>
 * Created by dbborens on 3/26/14.
 */
public class TimeReader {

    private double[] times;
    private int[] frames;

    public TimeReader(String root) {
        String filename = FileConventions.TIME_FILENAME;
        String absolute = root + filename;
        DataInputStream stream = FileConventions.makeDataInputStream(absolute);
        loadData(stream);
    }

    private void loadData(DataInputStream stream) {
        boolean eof = false;
        ArrayList<Double> timesList = new ArrayList<>();
        ArrayList<Integer> framesList = new ArrayList<>();

        do {
            try {
                int frame = stream.readInt();
                double time = stream.readDouble();
                timesList.add(time);
                framesList.add(frame);
            } catch (EOFException ex) {
                eof = true;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } while (!eof);

        // I think Apache Commons has something for this, but I don't feel
        // like loading an entire dependency just for that
        times = toDoubleArray(timesList);
        frames = toIntegerArray(framesList);
    }

    // Apache commons can do this automagically
    private double[] toDoubleArray(ArrayList<Double> toConvert) {
        int n = toConvert.size();
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = toConvert.get(i);
        }
        return ret;
    }

    // Apache commons can do this automagically
    private int[] toIntegerArray(ArrayList<Integer> toConvert) {
        int n = toConvert.size();
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = toConvert.get(i);
        }
        return ret;
    }


    public double[] getTimes() {
        return times;
    }

    public int[] getFrames() {
        return frames;
    }
}
