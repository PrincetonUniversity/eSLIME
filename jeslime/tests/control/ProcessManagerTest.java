package control;

import io.project.ProcessLoader;
import junit.framework.TestCase;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.*;
import structural.GeneralParameters;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class ProcessManagerTest extends TestCase {
    public void testGetTriggeredProcesses() throws Exception {

    }

    public void testTriggered() throws Exception {

    }

    private class ProcessManagerExposed extends ProcessManager {
        public ProcessManagerExposed(ProcessLoader processLoader, LayerManager lm, GeneralParameters p) {
            super(processLoader, lm, p);
        }

        @Override
        public boolean triggered(int n, processes.Process process) {
            return super.triggered(n, process);
        }
    }
}
