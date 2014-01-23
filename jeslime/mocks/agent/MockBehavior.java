package agent;

import agent.Behavior;
import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class MockBehavior extends Behavior {

    private int timesRun;
    private HashMap<Coordinate, Integer> callerCounts;

    public MockBehavior() {
        super(null, null, null);
        timesRun = 0;
        callerCounts = new HashMap<>();
    }

    @Override
    public void trigger(Coordinate caller) {
        timesRun++;

        if (caller != null) {
            increment(caller);
        }
    }

    private void increment(Coordinate caller) {
        if (!callerCounts.containsKey(caller)) {
            callerCounts.put(caller, 0);
        }

        int count = callerCounts.get(caller);
        callerCounts.put(caller, count+1);
    }

    public int getTimesRun() {
        return timesRun;
    }

    public int timesCaller(Coordinate caller) {
        if (!callerCounts.containsKey(caller))
            return 0;

        return callerCounts.get(caller);
    }
}
