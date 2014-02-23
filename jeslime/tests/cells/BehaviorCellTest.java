package cells;

import agent.action.Action;
import agent.action.MockAction;
import agent.control.BehaviorDispatcher;
import agent.control.MockBehaviorDispatcher;
import structural.identifiers.Coordinate;
import test.EslimeLatticeTestCase;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCellTest extends EslimeLatticeTestCase {

    private BehaviorCell query;
    private MockBehaviorDispatcher dispatcher;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dispatcher = new MockBehaviorDispatcher();
        query = new BehaviorCell(layerManager, 1, 1.0, 0.5);
        query.setDispatcher(dispatcher);
        layer.getUpdateManager().place(query, origin);
    }

    public void testConsiderAndApply() {
        int result = query.consider();
        assertEquals(1, result);

        result = query.consider();
        assertEquals(2, result);

        query.apply();

        result = query.consider();

        assertEquals(1, result);
    }

    public void testDivide() throws Exception {
        Cell child = query.divide();
        assertEquals(child, query);

        // Fitness should be half for each
        assertEquals(0.5, query.getFitness(), epsilon);
        assertEquals(0.5, child.getFitness(), epsilon);
    }

    public void testClone() throws Exception {
        Cell clone = query.clone();
        assertEquals(clone, query);

        // Since no division took place, fitness should be original for each
        assertEquals(1.0, query.getFitness(), epsilon);
        assertEquals(1.0, clone.getFitness(), epsilon);
    }

    public void testTrigger() throws Exception {
        String triggerName = "TEST";
        Coordinate caller = new Coordinate(0, 0, 0);
        query.trigger(triggerName, caller);

        assertEquals(triggerName, dispatcher.getLastTriggeredName());
        assertEquals(caller, dispatcher.getLastTriggeredCaller());
    }

    public void testDie() throws Exception {
        fail("Not yet implemented");
//        query.die();
//        assertTrue(dispatcher.died());
    }

    public void testEquals() {
        // Difference based on dispatcher (in)equality.
        BehaviorCell other = new BehaviorCell(null, 1, 1.0, 0.5);
        MockBehaviorDispatcher d2 = new MockBehaviorDispatcher();
        other.setDispatcher(d2);
        d2.setOverrideEquals(true);

        // ...unequal dispatcher.
        d2.setReportEquals(false);
        assertNotEquals(query, other);

        // ...equal dispatcher.
        d2.setReportEquals(true);
        assertEquals(query, other);

        // Test a cell that differs in division threshold.
        other = new BehaviorCell(null, 1, 1.0, 1.0);
        other.setDispatcher(d2);
        assertNotEquals(query, other);

        // Test a cell that differs in state.
        other = new BehaviorCell(null, 2, 1.0, 0.5);
        other.setDispatcher(d2);
        assertNotEquals(query, other);
    }

    /**
     * The base BehaviorCell class automatically marks itself
     * as divisible according to its fitness. So if a call
     * to setFitness() puts it above or below the threshold,
     * that should be noted.
     */
    public void testDivisibilityThresholding() {
        // Start off below threshold.
        
        // Adjust above threshold.

        // Adjust below threshold.

        fail("Not yet implemented.");
    }
}
