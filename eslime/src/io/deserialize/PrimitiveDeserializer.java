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
