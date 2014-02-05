package cells;

import agent.action.Action;
import agent.action.MockAction;
import agent.control.MockBehaviorDispatcher;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCellTest extends EslimeTestCase {

    private BehaviorCell query;
    private MockBehaviorDispatcher dispatcher;

    @Override
    protected void setUp() throws Exception {
        dispatcher = new MockBehaviorDispatcher();
        query = new BehaviorCell(1, 1.0, 0.5);
        query.setDispatcher(dispatcher);
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
        query.die();
        assertTrue(dispatcher.died());
    }
}
