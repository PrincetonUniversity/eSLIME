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

import layers.LightweightSystemState;
import structural.utilities.FileConventions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbborens on 3/28/14.
 */
public class ContinuumStateReaderManager {


    private HashMap<String, ContinuumStateReader> readers;

    public ContinuumStateReaderManager(String root, String[] ids) {
        readers = new HashMap<>(ids.length);
        for (String id : ids) {
            String filename = FileConventions.makeContinuumStateFilename(id);
            String absoluteFilename = root + filename;
            System.out.println(absoluteFilename);
            File file = new File(absoluteFilename);
            ContinuumStateReader reader = new ContinuumStateReader(file);
            readers.put(id, reader);
        }
    }

    public Map<String, double[]> next() {
        Map<String, double[]> ret = new HashMap<>(readers.size());

        for (String id : readers.keySet()) {
            ContinuumStateReader reader = readers.get(id);
            double[] state = reader.next();
            ret.put(id, state);
        }

        return ret;
    }

    /**
     * Load next frame into lightweight system state object.
     */
    public void populate(LightweightSystemState systemState) {
        Map<String, double[]> continuumStateMap = next();

        for (String id : continuumStateMap.keySet()) {
            double[] soluteVector = continuumStateMap.get(id);
            systemState.initSoluteLayer(id, soluteVector);
        }
    }
}
