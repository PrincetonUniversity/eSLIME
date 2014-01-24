package agent.control;

import agent.Behavior;
import cells.Cell;
import io.project.BehaviorLoader;
import layers.LayerManager;
import org.dom4j.Element;
import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * BehaviorDispatcher is a map between behavior names and the
 * behaviors themselves. It is associated with a particular cell,
 * and can be used to trigger behaviors in that cell.
 *
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorDispatcher {
    protected HashMap<String, Behavior> behaviors;

    public BehaviorDispatcher() {
        behaviors = new HashMap<>();
    }

    public BehaviorDispatcher(Element behaviorRoot, Cell callback, LayerManager layerManager) {
        BehaviorLoader loader = new BehaviorLoader(this, callback, layerManager);
        loader.loadAllBehaviors(behaviorRoot);
    }

    public void map(String name, Behavior behavior) {
        behaviors.put(name, behavior);
    }

    /**
     * Trigger a behavior associated with the cell.
     *
     * @param behaviorName
     * @param caller The coordinate from which the call originated. If
     *               the call originated with a top-down process, the
     *               caller will be null.
     */
    public void trigger(String behaviorName, Coordinate caller) {
        if (!behaviors.containsKey(behaviorName)) {
            throw new IllegalArgumentException("Behavior '" + behaviorName + "' not found.");
        }

        Behavior behavior = behaviors.get(behaviorName);
        behavior.run(caller);
    }
}
