/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import test.LinearMocks;

import java.util.Random;

import static org.mockito.Mockito.*;
/**
 * Created by dbborens on 3/6/14.
 */
public class StochasticChoiceTest extends LinearMocks {

    private DynamicActionRangeMap chooser;
    private Random random;
    private Action action;
    private StochasticChoice query;

    @Before
    public void init() throws Exception {
        action = mock(Action.class);

        random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.5);

        chooser = mock(DynamicActionRangeMap.class);
        when(chooser.getTotalWeight()).thenReturn(5.0);
        when(chooser.selectTarget(2.5)).thenReturn(action);
        when(chooser.clone(any())).thenReturn(chooser);
        query = new StochasticChoice(null, null, chooser, random);
    }

    @Test
    public void runCalculatesCorrectValue() throws Exception {
        query.run(a);
        verify(chooser).selectTarget(2.5);
    }

    @Test
    public void runRefreshesChooserBeforeUsing() throws Exception {
        InOrder inOrder = inOrder(chooser);
        query.run(a);
        inOrder.verify(chooser).refresh();
        inOrder.verify(chooser).getTotalWeight();
    }

    @Test
    public void runTriggersSelectionWithCaller() throws Exception {
        doTriggerTest(query);
    }

    @Test
    public void cloneBehavesAsExpected() throws Exception {
        BehaviorCell child = mock(BehaviorCell.class);
        Action clone = query.clone(child);
        verify(chooser).clone(child);
        doTriggerTest(clone);
    }

    private void doTriggerTest(Action target) throws Exception {
        target.run(a);
        verify(action).run(a);
    }
}
