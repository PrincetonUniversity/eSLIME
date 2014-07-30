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

package structural.utilities;

import java.io.*;

/**
 * Created by dbborens on 3/26/14.
 */
public abstract class FileConventions {
    public final static String COORDINATE_FILENAME = "coordinates.txt";

    public final static String CONTINUUM_STATE_PREFIX = "solute";
    public final static String CONTINUUM_STATE_SUFFIX = ".state.bin";
    public final static String CONTINUUM_METADATA_SUFFIX = ".metadata.txt";

    public final static String HIGHLIGHT_PREFIX = "channel";
    public final static String HIGHLIGHT_SUFFIX = ".highlight.bin";

    public static final String TIME_FILENAME = "clock.bin";

    public static final String INTERFACE_PREFIX = "interface_";
    public static final String INTERFACE_SUFFIX = ".txt";

    public static String makeContinuumStateFilename(String soluteId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CONTINUUM_STATE_PREFIX);
        sb.append(soluteId);
        sb.append(CONTINUUM_STATE_SUFFIX);
        return sb.toString();
    }

    public static String makeInterfaceFilename(Integer focalState) {
        StringBuilder sb = new StringBuilder();
        sb.append(INTERFACE_PREFIX);
        sb.append(focalState);
        sb.append(INTERFACE_SUFFIX);
        return sb.toString();
    }
    public static String makeContinuumMetadataFilename(String soluteId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CONTINUUM_STATE_PREFIX);
        sb.append(soluteId);
        sb.append(CONTINUUM_METADATA_SUFFIX);
        return sb.toString();
    }

    public static DataInputStream makeDataInputStream(String fullPath) {
        try {
            return doMakeDataInputSteam(fullPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static DataInputStream doMakeDataInputSteam(String fullPath)
            throws IOException {

        File file = new File(fullPath);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

        return dataInputStream;
    }

    public static String makeHighlightFilename(int channel) {
        StringBuilder sb = new StringBuilder();
        sb.append(HIGHLIGHT_PREFIX);
        sb.append(channel);
        sb.append(HIGHLIGHT_SUFFIX);
        return sb.toString();
    }

    public static DataOutputStream makeDataOutputStream(String fullPath) {
        try {
            return doMakeDataOutputStream(fullPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static DataOutputStream doMakeDataOutputStream(String fullPath) throws IOException {
        File file = new File(fullPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

        return dataOutputStream;
    }
}
