package agent.control;

import agent.Behavior;
import cells.BehaviorCell;
import cells.Cell;
import io.project.BehaviorLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import structural.GeneralParameters;
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
    private BehaviorCell callback;
    private LayerManager layerManager;
    private HashMap<String, Behavior> behaviors;
    private GeneralParameters p;

    public BehaviorDispatcher() {
        behaviors = new HashMap<>();
    }

    public BehaviorDispatcher(BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        behaviors = new HashMap<>();
        this.layerManager = layerManager;
        this.callback = callback;
        this.p = p;
    }

    public BehaviorDispatcher(Element behaviorRoot, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        this.p = p;
        behaviors = new HashMap<>();
        BehaviorLoader loader = new BehaviorLoader(this, callback, layerManager, p);
        loader.loadAllBehaviors(behaviorRoot);

        this.layerManager = layerManager;
        this.callback = callback;
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

    public BehaviorDispatcher clone(BehaviorCell child) {
        BehaviorDispatcher clone = new BehaviorDispatcher();

        // Clone the behavior catalog item for item.
        for (String behaviorName : behaviors.keySet()) {
            Behavior b = behaviors.get(behaviorName);
            Behavior bc = b.clone(child);
            clone.map(behaviorName, bc);
        }

        return clone;
    }

    /**
     * A BehaviorDispatcher is equal to another object only if:
     *    (1) The other Object is a BehaviorDispatcher.
     *    (2) Each Behavior in the other BehaviorDispatcher
     *        has an equivalent Behavior mapped to the same name
     *        as this BehaviorDispatcher.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BehaviorDispatcher)) {
            return false;
        }

        BehaviorDispatcher other = (BehaviorDispatcher) obj;

        if (other.behaviors.size() != this.behaviors.size()) {
            return false;
        }

        for (String behaviorName : behaviors.keySet()) {
            if (!other.behaviors.containsKey(behaviorName)) {
                return false;
            }

            Behavior otherBehavior = other.behaviors.get(behaviorName);
            Behavior thisBehavior = this.behaviors.get(behaviorName);

            if (!thisBehavior.equals(otherBehavior)) {
                return false;
            }
        }

        return true;
    }

    public Behavior getMappedBehavior(String behaviorName) {
        return behaviors.get(behaviorName);
    }

}
