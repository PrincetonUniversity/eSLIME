package agent.action;

import cells.Cell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 1/22/14.
 */
public class MockAction extends Action {
    private int timesRun;
    private HashMap<Coordinate, Integer> callerCounts;

    public Coordinate getLastCaller() {
        return lastCaller;
    }

    private Coordinate lastCaller;

    @Override
    public Action clone(Cell child) {
        return new MockAction();
    }

    public MockAction() {
        super(null, null);
        timesRun = 0;
        callerCounts = new HashMap<>();
    }

    @Override
    public void run(Coordinate caller) {
        timesRun++;
        lastCaller = caller;
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

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockAction);
    }

    public int timesCaller(Coordinate caller) {
        if (!callerCounts.containsKey(caller))
            return 0;

        return callerCounts.get(caller);
    }

}
