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
        List<EcoProcess> processes = ProcessListFactory.instantiate(root, lm, p);
        ProcessManager processManager = new ProcessManager(processes, lm);
        return processManager;
    }

}
