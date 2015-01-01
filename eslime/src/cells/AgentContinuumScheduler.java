/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.identifiers.Coordinate;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.ContinuumAgentNotifier;
import structural.Trigger;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dbborens on 12/31/14.
 */
public class AgentContinuumScheduler {

    private BehaviorCell cell;

    private Function<String, ContinuumAgentLinker> retrieveLinker;
    private Supplier<Coordinate> locate;

    private AgentContinuumIndex inj;
    private AgentContinuumIndex exp;

    public AgentContinuumScheduler(Function<String, ContinuumAgentLinker> retrieveLinker,
                                   BehaviorCell cell,
                                   Supplier<Coordinate> locate,
                                   AgentContinuumIndex inj,
                                   AgentContinuumIndex exp) {

        this.retrieveLinker = retrieveLinker;
        this.cell = cell;
        this.locate = locate;
        this.inj = inj;
        this.exp = exp;
    }

    public void scheduleInj(String id, double magnitude) {
        Function<ContinuumAgentLinker, ContinuumAgentNotifier> resolveNotifier =
                linker -> linker.getInjNotifier();

        doSchedule(inj, resolveNotifier, id, magnitude);
    }

    public void scheduleExp(String id, double magnitude) {
        Function<ContinuumAgentLinker, ContinuumAgentNotifier> resolveNotifier =
                linker -> linker.getExpNotifier();

        doSchedule(exp, resolveNotifier, id, magnitude);
    }

    private void doSchedule(AgentContinuumIndex index,
                            Function<ContinuumAgentLinker, ContinuumAgentNotifier> resolveNotifier,
                            String id,
                            double magnitude) {

        ContinuumAgentLinker linker = retrieveLinker.apply(id);
        Coordinate coord = locate.get();
        ContinuumAgentNotifier notifier = resolveNotifier.apply(linker);
        Trigger remove = () -> notifier.remove(cell);
        Supplier<Double> getValue = () -> linker.get(coord);
        ContinuumLinkage linkage = new ContinuumLinkage(remove, getValue, magnitude);
        index.put(id, linkage);
    }

}
