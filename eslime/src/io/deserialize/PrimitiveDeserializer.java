/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import control.identifiers.Coordinate;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by David B Borenstein on 3/25/14.
 */
public abstract class PrimitiveDeserializer {
    public static double[] readDoubleVector(DataInputStream input) throws IOException {
        // Length is encoded, redundantly, in each vector.
        int length = input.readInt();

        double[] ret = new double[length];

        for (int i = 0; i < length; i++) {
            double value = input.readDouble();
            ret[i] = value;
        }

        return ret;
    }

    public static int[] readIntegerVector(DataInputStream input) throws IOException {
        // Length is encoded, redundantly, in each vector.
        int length = input.readInt();

        int[] ret = new int[length];

        for (int i = 0; i < length; i++) {
            int value = input.readInt();
            ret[i] = value;
        }

        return ret;
    }

    public static boolean[] readBooleanVector(DataInputStream input) throws IOException {
        // Length is encoded, redundantly, in each vector.
        int length = input.readInt();

        boolean[] ret = new boolean[length];

        for (int i = 0; i < length; i++) {
            boolean value = input.readBoolean();
            ret[i] = value;
        }

        return ret;
    }

    public static Coordinate[] readCoordinateVector(DataInputStream input,
                                                    CoordinateDeindexer deindex)
            throws IOException {

        // Length is encoded, redundantly, in each vector.
        int length = input.readInt();

        Coordinate[] ret = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            int index = input.readInt();
            Coordinate coord = deindex.getCoordinate(index);
            ret[i] = coord;
        }

        return ret;
    }
}
