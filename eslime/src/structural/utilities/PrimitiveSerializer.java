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

package structural.utilities;

import control.identifiers.Coordinate;
import geometry.Geometry;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods for serializing vectors of primitive data types.
 * <p/>
 * Created by David B Borenstein on 3/25/14.
 */
public abstract class PrimitiveSerializer {

    /**
     * Append a vector of double values to a data output stream.
     *
     * @param dataOutputStream
     * @param vector
     */
    public static void writeDoubleVector(DataOutputStream dataOutputStream, List<Double> vector) {

        try {
            // Encode length (as a reality check)
            int length = vector.size();
            dataOutputStream.writeInt(length);
            for (int i = 0; i < vector.size(); i++) {
                double datum = vector.get(i);
                dataOutputStream.writeDouble(datum);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeDoubleVector(DataOutputStream dataOutputStream, double[] vector) {
        List<Double> list = new ArrayList<Double>(vector.length);

        for (double datum : vector) {
            list.add(datum);
        }

        writeDoubleVector(dataOutputStream, list);
    }

    /**
     * Append a vector of integer values to a data output stream.
     *
     * @param dataOutputStream
     * @param vector
     */
    public static void writeIntegerVector(DataOutputStream dataOutputStream, List<Integer> vector) {
        try {
            // Encode length (as a reality check)
            int length = vector.size();
            dataOutputStream.writeInt(length);
            for (int i = 0; i < vector.size(); i++) {
                Integer datum = vector.get(i);
                dataOutputStream.writeInt(datum);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Append a vector of boolean values to a data outpute stream.
     *
     * @param dataOutputStream
     * @param vector
     */
    public static void writeBooleanVector(DataOutputStream dataOutputStream,
                                          List<Boolean> vector) {
        try {
            // Encode length (as a reality check)
            int length = vector.size();
            dataOutputStream.writeInt(length);
            for (int i = 0; i < vector.size(); i++) {
                Boolean datum = vector.get(i);
                dataOutputStream.writeBoolean(datum);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Convert a vector of coordinates to integers, and then append to a
     * data output stream.
     */
    public static void writeCoercedCoordinateVector(DataOutputStream dataOutputStream,
                                                    List<Coordinate> vector,
                                                    Geometry geometry) {

        ArrayList<Integer> intVector = new ArrayList<>(vector.size());
        for (Coordinate coord : vector) {
            Integer index = geometry.coordToIndex(coord);
            intVector.add(index);
        }

        writeIntegerVector(dataOutputStream, intVector);
    }
}
