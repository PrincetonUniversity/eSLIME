package control;

import io.serialize.MockSerializationManager;
import junit.framework.TestCase;
import structural.MockGeneralParameters;
import structural.halt.HaltCondition;
import structural.halt.StepMaxReachedEvent;
/**
 * Created by David B Borenstein on 1/7/14.
 */
public class IntegratorTest extends TestCase {

    // Items used during construction
    private MockGeneralParameters p;
    private MockSerializationManager sm;
    private MockProcessManager mgr;

    // And now, the thing to be tested...
    private Integrator integrator;

    @Override
    protected void setUp() throws Exception {
        // Initialize infrastructure objects
        p = new MockGeneralParameters();
        sm = new MockSerializationManager();
        mgr = new MockProcessManager();
        integrator = new Integrator(p, mgr, sm);
    }

    public void testGo() throws Exception {
        // Set T to 5 loops.
        p.setT(5);

        // Each of the following should have been called 5 times:
        HaltCondition halt = integrator.go();

        assertTrue(halt instanceof StepMaxReachedEvent);
        assertEquals(5, mgr.getTimesIterated());
    }
}
