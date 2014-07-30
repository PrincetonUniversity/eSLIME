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

import control.identifiers.Extrema;
import control.identifiers.TemporalCoordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads an extrema file, which is a format used
 * by several writer classes.
 */
public class ExtremaReader {


    HashMap<String, Extrema> data;

    public ExtremaReader(File input) {
        data = new HashMap<String, Extrema>();
        // Read the metadata file to get the extrema
        try {
            extractMetadata(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Extrema get(String key) {
        return data.get(key);
    }

    private void extractMetadata(File metadataFile) throws IOException {
        FileReader mfr = new FileReader(metadataFile);
        BufferedReader mbr = new BufferedReader(mfr);
        String next = mbr.readLine();


        while (next != null) {
            next = next.trim();

            // So inelegant...
            String[] mapping = next.split(">");
            String key = mapping[0];
            String value = mapping[1];

            Extrema extrema = loadExtrema(value);
            data.put(key, extrema);
            next = mbr.readLine();
        }
    }

    private Extrema loadExtrema(String tokenize) {
        String[] minMax = tokenize.split(":");
        String[] minArg = minMax[0].split("@");
        String[] maxArg = minMax[1].split("@");

        Double min = Double.valueOf(minArg[0]);
        Double max = Double.valueOf(maxArg[0]);

        String minToken = minArg[1];
        String maxToken = maxArg[1];

        TemporalCoordinate argMin = parseTemporalCoordinate(minToken);
        TemporalCoordinate argMax = parseTemporalCoordinate(maxToken);

        Extrema ret = new Extrema();
        ret.load(min, argMin, max, argMax);

        return ret;
    }

    private TemporalCoordinate parseTemporalCoordinate(String token) {
        String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+) \\| (\\d+\\.\\d+)\\)");
        TemporalCoordinate c;
        Pattern pattern = Pattern.compile(pStr);
        Matcher matcher = pattern.matcher(token);
        matcher.find();

        int x = Integer.valueOf(matcher.group(1));
        int y = Integer.valueOf(matcher.group(2));
        int flags = Integer.valueOf(matcher.group(5));
        double t = Double.valueOf(matcher.group(6));

        if (matcher.group(4) != null) {
            // (1, 2, 3 | 4 | 5.0)
            int z = Integer.valueOf(matcher.group(4));
            c = new TemporalCoordinate(x, y, z, t, flags);
        } else {
            // (1, 2 | 3 | 4.0)
            c = new TemporalCoordinate(x, y, t, flags);
        }

        return c;
    }
}
