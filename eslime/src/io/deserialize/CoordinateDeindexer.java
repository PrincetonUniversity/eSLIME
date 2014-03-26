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

import structural.identifiers.Coordinate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateDeindexer {

    // This file specifies the relationship between vector index and coordinate.
    private final String COORDMAP_FILENAME = "coordmap.txt";

    private String path;

    protected HashMap<Integer, Coordinate> indexToCoord;
    protected HashMap<Coordinate, Integer> coordToIndex;

    /**
     * Default constructor for testing only!
     */
    public CoordinateDeindexer() {
    }

    public CoordinateDeindexer(String path) {

        this.path = path;

        try {
            deindex();
        } catch (FileNotFoundException e) {
            String msg = "eSLIME attempted to build a coordinate de-indexer, "
                    + "but was unable to find a coordinate index file. This usually "
                    + "happens when a post-processing step, such as a visualization, "
                    + "requires an index but the user did not include a "
                    + "<coordinate-indexer /> argument in the <serializers> section "
                    + "of the script.";

            throw new IllegalArgumentException(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinate getCoordinate(Integer index) {
        return indexToCoord.get(index);
    }

    public Integer getIndex(Coordinate coord) {
        return coordToIndex.get(coord);
    }

    protected void deindex() throws IOException {
        ArrayList<Coordinate> coordList = new ArrayList<Coordinate>();
        indexToCoord = new HashMap<Integer, Coordinate>();
        coordToIndex = new HashMap<Coordinate, Integer>();

        String fn = path + '/' + COORDMAP_FILENAME;
        FileReader mfr = new FileReader(fn);
        BufferedReader mbr = new BufferedReader(mfr);
        String next = mbr.readLine();

        while (next != null) {
            // Lines are in the form
            // 		1	(0, 1 | 1)
            // 		2	(0, 2 | 1)
            next = next.trim();
            String[] mapping = next.split("\t");
            Integer key = Integer.valueOf(mapping[0]);

            String token = mapping[1];
            Coordinate c = parseCoordinate(token);

            indexToCoord.put(key, c);
            coordToIndex.put(c, key);

            next = mbr.readLine();

        }

    }

    protected Coordinate parseCoordinate(String token) {
        String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+)\\)");

        Coordinate c;
        Pattern pattern = Pattern.compile(pStr);
        Matcher matcher = pattern.matcher(token);
        matcher.find();

        int x = Integer.valueOf(matcher.group(1));
        int y = Integer.valueOf(matcher.group(2));
        int flags = Integer.valueOf(matcher.group(5));

        if (matcher.group(4) != null) {
            // (1, 2, 3 | 4)
            int z = Integer.valueOf(matcher.group(4));
            c = new Coordinate(x, y, z, flags);
        } else {
            // (1, 2 | 3)
            c = new Coordinate(x, y, flags);
        }

        return c;
    }
}
