package processes.cellular;

import geometry.Geometry;
import io.project.ProcessLoader;
import layers.cell.CellLayer;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Removes all cells in the active area.
 *
 * Created by David B Borenstein on 12/24/13.
 */
public class Smite extends CellProcess {

    private boolean skipDead;
    public Smite(ProcessLoader loader, CellLayer layer, int id, Geometry geom, GeneralParameters p) {
        super(loader, layer, id, geom, p);

        skipDead = Boolean.valueOf(get("skip-dead-sites"));
    }

    // Minimal constructor for mock tests
    public Smite(CellLayer layer, Geometry geom, boolean skipDead) {
        super(null, layer, 0, geom, null);
        this.skipDead = skipDead;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // This process only has one event: it affects all relevant cells.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        for (Coordinate c : activeSites) {
            boolean dead = !layer.getViewer().isOccupied(c);

            // If it's dead and we don't expect already-dead cells, throw error
            if (dead && !skipDead) {
                String msg = "Attempted to smite coordinate " + c.toString() +
                        " but it was already dead. This is illegal unless" +
                        " the <skip-dead-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!dead) {
                layer.getUpdateManager().banish(c);
            } else {
                // Do nothing if site is filled and skipDead is true.
            }
        }
    }


}
