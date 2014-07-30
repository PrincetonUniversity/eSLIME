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

package io.serialize;

import control.identifiers.Coordinate;
import geometry.MockGeometry;
import structural.utilities.PrimitiveSerializer;
import test.EslimeTestCase;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David B Borenstein on 3/25/14.
 */
public class PrimitiveSerializerTest extends EslimeTestCase {
    public void testWriteDoubleVector() throws Exception {
        DataOutputStream output = makeOutput("doubleListVector.bin");
        List<Double> test = new ArrayList<>(5);
        test.add(5.1);
        test.add(-3.0);
        test.add(0.0);
        test.add(-0.7);
        test.add(1e-7);
        PrimitiveSerializer.writeDoubleVector(output, test);
        output.close();
        assertBinaryFilesEqual("doubleVector.bin", "doubleListVector.bin");
    }

    public void testWriteDoubleVector1() throws Exception {
        DataOutputStream output = makeOutput("doubleArrayVector.bin");
        double[] test = new double[]{5.1, -3.0, 0.0, -0.7, 1e-7};
        PrimitiveSerializer.writeDoubleVector(output, test);
        output.close();
        assertBinaryFilesEqual("doubleVector.bin", "doubleArrayVector.bin");
    }

    public void testWriteIntegerVector() throws Exception {
        DataOutputStream output = makeOutput("integerVector.bin");
        List<Integer> test = new ArrayList<>(5);
        test.add(5);
        test.add(-3);
        test.add(0);
        test.add(-7);
        test.add(2);
        PrimitiveSerializer.writeIntegerVector(output, test);
        output.close();
        assertBinaryFilesEqual("integerVector.bin");
    }

    public void testWriteBooleanVector() throws Exception {
        DataOutputStream output = makeOutput("booleanVector.bin");
        List<Boolean> test = new ArrayList<>(5);
        test.add(true);
        test.add(false);
        test.add(true);
        test.add(true);
        test.add(false);
        PrimitiveSerializer.writeBooleanVector(output, test);
        output.close();
        assertBinaryFilesEqual("booleanVector.bin");
    }

    private DataOutputStream makeOutput(String filename) throws Exception {
        String fullName = this.outputPath + filename;
        File file = new File(fullName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        DataOutputStream output = new DataOutputStream(bufferedOutputStream);
        return output;
    }

    public void testWriteCoercedCoordinateVector() throws Exception {
        DataOutputStream output = makeOutput("coordinateVector.bin");
        MockGeometry geom = buildMockGeometry();
        List<Coordinate> test = new ArrayList<>(5);
        for (int i = 3; i <= 0; i--) {
            Coordinate coord = geom.getCanonicalSites()[i];
            test.add(coord);
        }
        test.add(geom.getCanonicalSites()[2]);
        test.add(geom.getCanonicalSites()[1]);
        test.add(geom.getCanonicalSites()[0]);
        test.add(geom.getCanonicalSites()[3]);
        test.add(geom.getCanonicalSites()[2]);
        PrimitiveSerializer.writeCoercedCoordinateVector(output, test, geom);
        output.close();
        assertBinaryFilesEqual("coordinateVector.bin");
    }
}
