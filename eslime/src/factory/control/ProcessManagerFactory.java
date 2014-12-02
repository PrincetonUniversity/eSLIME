/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control;

import control.GeneralParameters;
import control.ProcessManager;
import factory.processes.ProcessListFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.EcoProcess;

import java.util.List;

/**
 * Created by dbborens on 11/26/14.
 */
public abstract class ProcessManagerFactory {

    public static ProcessManager instantiate(Element root, GeneralParameters p, LayerManager lm) {
        if (root == null) {
            return nullCase(lm, p);
        }
        Element processElem = root.element("processes");
        List<EcoProcess> processes = ProcessListFactory.instantiate(processElem, lm, p);
        ProcessManager processManager = new ProcessManager(processes, lm);
        return processManager;
    }

    private static ProcessManager nullCase(LayerManager lm, GeneralParameters p) {
        List<EcoProcess> processes = ProcessListFactory.instantiate(null, lm, p);
        ProcessManager processManager = new ProcessManager(processes, lm);
        return processManager;
    }

}
