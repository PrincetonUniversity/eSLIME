/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes;

import control.GeneralParameters;
import layers.LayerManager;
import org.dom4j.Element;
import processes.EcoProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class ProcessListFactory {

    public static List<EcoProcess> instantiate(Element root, LayerManager layerManager, GeneralParameters p) {
        ArrayList<EcoProcess> processes = new ArrayList<>();
        if (root == null) {
            return processes;
        }
        int id = 0;
        for (Object o : root.elements()) {
            Element e = (Element) o;
            EcoProcess process = ProcessFactory.instantiate(e, layerManager, p, id);
            processes.add(process);
            id++;
        }
        return processes;
    }
}
