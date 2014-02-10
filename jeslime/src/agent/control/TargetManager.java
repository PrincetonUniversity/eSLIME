package agent.control;

import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Resolves a target descriptor at construction time and returns
 * a list of target Cells on demand.
 *
 * Created by dbborens on 2/7/14.
 *
 */
public class TargetManager {

    private Cell callback;
    private LayerManager layerManager;

    public TargetManager(Cell callback, LayerManager layerManager, Element descriptor) {
        this.callback = callback;
        this.layerManager = layerManager;


        // Load a targetter

    }

    // Create a passthrough for target.report()
}
