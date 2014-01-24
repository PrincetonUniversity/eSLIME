package agent.control;

import agent.Behavior;
import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;
import structural.identifiers.Coordinate;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public class MockBehaviorDispatcher extends BehaviorDispatcher {

    private String lastTriggeredName;
    private Coordinate lastTriggeredCaller;
    private String lastMappedName;
    private Behavior lastMappedBehavior;
    private ArrayList<String> mappedNames;
    private ArrayList<Behavior> mappedBehaviors;

    public MockBehaviorDispatcher() {
        mappedNames = new ArrayList<>();
        mappedBehaviors = new ArrayList<>();
   }

    public ArrayList<String> getMappedNames() {
        return mappedNames;
    }

    public ArrayList<Behavior> getMappedBehaviors() {
        return mappedBehaviors;
    }

    public Behavior getLastMappedBehavior() {
        return lastMappedBehavior;
    }

    public String getLastMappedName() {
        return lastMappedName;
    }

    public Coordinate getLastTriggeredCaller() {
        return lastTriggeredCaller;
    }

    public String getLastTriggeredName() {
        return lastTriggeredName;
    }

    @Override
    public void map(String name, Behavior behavior) {
        mappedNames.add(name);
        mappedBehaviors.add(behavior);
        lastMappedName = name;
        lastMappedBehavior = behavior;
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        lastTriggeredName = behaviorName;
        lastTriggeredCaller = caller;
    }
}
