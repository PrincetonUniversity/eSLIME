/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import factory.processes.ProcessFactory;
import processes.Process;

/**
 * Created by dbborens on 1/14/14.
 */
public class MockProcessFactory extends ProcessFactory {
    private Process[] processes;
    private Process instantiateReturn;

    public MockProcessFactory() {
        super(null, null, null);
    }

    public Process[] getProcesses() {
        return processes;
    }

    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

    public void setInstantiateReturn(Process instantiateReturn) {
        this.instantiateReturn = instantiateReturn;
    }

    @Override
    public Process instantiate(Integer id) {
        return instantiateReturn;
    }
}
