/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

/**
 * Created by dbborens on 3/28/14.
 */
public class ContinuumStateReaderManager {


    private HashMap<String, ContinuumStateReader> readers;

    public ContinuumStateReaderManager(String root, String[] ids) {
        throw new NotImplementedException();
//        readers = new HashMap<>(ids.length);
//        for (String id : ids) {
//            String filename = FileConventions.makeContinuumStateFilename(id);
//            String absoluteFilename = root + filename;
//            System.out.println(absoluteFilename);
//            File file = new File(absoluteFilename);
//            ContinuumStateReader reader = new ContinuumStateReader(file);
//            readers.put(id, reader);
//        }
    }

//    public Map<String, double[]> next() {
//        Map<String, double[]> ret = new HashMap<>(readers.size());
//
//        for (String id : readers.keySet()) {
//            ContinuumStateReader reader = readers.get(id);
//            double[] state = reader.next();
//            ret.put(id, state);
//        }
//
//        return ret;
//    }

//    /**
//     * Load next frame into lightweight system state object.
//     */
//    public void populate(LightweightSystemState systemState) {
//        Map<String, double[]> continuumStateMap = next();
//
//        for (String id : continuumStateMap.keySet()) {
//            double[] soluteVector = continuumStateMap.get(id);
//            systemState.initSoluteLayer(id, soluteVector);
//        }
//    }
}
