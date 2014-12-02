/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes;

import control.GeneralParameters;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import processes.EcoProcess;
import processes.MockProcess;
import processes.discrete.*;
import processes.discrete.check.CheckForDomination;
import processes.discrete.check.CheckForExtinction;
import processes.discrete.check.CheckForFixation;
import processes.discrete.check.CheckForThresholdOccupancy;
import processes.temporal.ExponentialInverse;
import processes.temporal.Tick;
import test.EslimeLatticeTestCase;

public class ProcessFactoryTest extends EslimeLatticeTestCase {

    private GeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
    }

    private EcoProcess make(String nodeName) {
        Element element = new BaseElement(nodeName);
        EcoProcess process = ProcessFactory.instantiate(element, layerManager, p, 0);
        return process;
    }

    private void doTest(String nodeName, Class expected) {
        EcoProcess process = make(nodeName);
        Class actual = process.getClass();
        assertEquals(expected, actual);
    }
    public void testExponentialInverse() throws Exception {
        doTest("exponential-inverse", ExponentialInverse.class);
    }

    public void testTick() throws Exception {
        doTest("tick", Tick.class);
    }

    public void testDivide() throws Exception {
        doTest("divide", Divide.class);
    }

    public void testOccupiedNeighborSwap() throws Exception {
        doTest("occupied-neighbor-swap", OccupiedNeighborSwap.class);
    }

    public void testGeneralNeighborSwap() throws Exception {
        doTest("general-neighbor-swap", GeneralNeighborSwap.class);
    }

    public void testScatter() throws Exception {
        doTest("scatter", Scatter.class);
    }

    public void testFill() throws Exception {
        doTest("fill", Fill.class);
    }

    public void testMockProcess() throws Exception {
        doTest("mock-process", MockProcess.class);
    }

    public void testTrigger() throws Exception {
        doTest("trigger", TriggerProcess.class);
    }

    public void testCull() throws Exception {
        doTest("cull", Cull.class);
    }

    public void testDiagnostic() throws Exception {
        doTest("diagnostic", DiagnosticProcess.class);
    }

    public void testCheckForFixation() throws Exception {
        doTest("check-for-fixation", CheckForFixation.class);
    }

    public void testCheckThresholdOccupancy() throws Exception {
        doTest("check-threshold-occupancy", CheckForThresholdOccupancy.class);
    }

    public void testCheckForDomination() throws Exception {
        doTest("check-for-domination", CheckForDomination.class);
    }

    public void testCheckForExtinction() throws Exception {
        doTest("check-for-extinction", CheckForExtinction.class);
    }

    public void testRecord() throws Exception {
        doTest("record", Record.class);
    }
}