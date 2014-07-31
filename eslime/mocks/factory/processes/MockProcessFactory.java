/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes;

import processes.Process;

/**
 * Created by dbborens on 7/31/14.
 */
public class MockProcessFactory extends ProcessFactory {
    private Process[] processes;

    public MockProcessFactory() {
        super(null, null, null);
    }

    @Override
    public Process[] getProcesses() {
        return processes;
    }

    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }
}
